package me.khmoon.googlecalendarslackbot.calendar;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.khmoon.googlecalendarslackbot.calendar.domain.CalendarEvents;
import me.khmoon.googlecalendarslackbot.calendar.domain.CalendarId;
import me.khmoon.googlecalendarslackbot.calendar.domain.ReservationDateTime;
import me.khmoon.googlecalendarslackbot.calendar.exception.DeletingEventFailedException;
import me.khmoon.googlecalendarslackbot.calendar.exception.FetchingEventsFailedException;
import me.khmoon.googlecalendarslackbot.calendar.exception.InsertingEventFailedException;
import me.khmoon.googlecalendarslackbot.calendar.exception.UpdatingEventFailedException;
import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.common.ReservationDetails;
import me.khmoon.googlecalendarslackbot.service.RestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarService {

  protected static final String CANCELLED_EVENT_STATUS = "cancelled";

  @Value("${calendar.summary.delimiter:/}")
  private String summaryDelimiter;

  private final Calendar calendar;
  private final RestService restService;

  public CalendarEvents findEvents(final ReservationDateTime fetchingDate, final CalendarId calendarId) {
    try {
      log.debug("find by date : fetching date = {}", fetchingDate);
      Events results = fetchEventsByCalendarId(fetchingDate, calendarId);

      return new CalendarEvents(
              results.getItems().stream()
                      .filter(this::isNotCancelled)
                      .collect(Collectors.toList())
      );
    } catch (IOException e) {
      throw new FetchingEventsFailedException(e);
    }
  }

  private Events fetchEventsByCalendarId(final ReservationDateTime fetchingDate, final CalendarId calendarId) throws IOException {
    Calendar.Events.List eventList = findListByCalendarId(calendarId);

    return eventList.setTimeMin(fetchingDate.getStartDateTime())
            .setTimeMax(fetchingDate.getEndDateTime())
            .setSingleEvents(true)
            .execute();
  }

  private Calendar.Events.List findListByCalendarId(final CalendarId calendarId) throws IOException {
    Calendar.Events eventsInCalendar = calendar.events();

    return eventsInCalendar.list(calendarId.getId());
  }

  public Optional<Event> findEventById(final String eventId, final CalendarId calendarId) {
    try {
      log.debug("find by id : fetching event id = {}", eventId);
      Event fetchedEvent = calendar.events()
              .get(calendarId.getId(), eventId)
              .execute();

      if (isCancelled(fetchedEvent)) {
        log.debug("event was cancelled : event id = {}", fetchedEvent.getId());
        return Optional.empty();
      }

      return Optional.of(fetchedEvent);
    } catch (IOException e) {
      throw new FetchingEventsFailedException(e);
    }
  }

  private boolean isCancelled(final Event fetchedEvent) {
    return CANCELLED_EVENT_STATUS.equals(fetchedEvent.getStatus());
  }

  private boolean isNotCancelled(final Event fetchedEvent) {
    return !isCancelled(fetchedEvent);
  }

  public Event insertEvent(final ReservationDateTime fetchingDate, ReservationDetails reservationDetails,
                           final CalendarId calendarId) {
    try {
      log.debug("insert : fetching date = {}, details = {}", fetchingDate, reservationDetails);

      MeetingRoom room = reservationDetails.getMeetingRoom();
      CalendarEvents calendarEvents = findEvents(fetchingDate, calendarId);
      checkExistenceOfMeetingRoom(room, calendarEvents);

      Event newEvent = createEventWith(fetchingDate, reservationDetails);

      Event insertedEvent = calendar.events()
              .insert(calendarId.getId(), newEvent)
              .execute();
      restService.sendText(insertedEvent.getSummary() + " " + fetchingDate.toKoreanString() + " 등록 완료 되었습니다.");
      log.debug("inserted event : event id = {}", insertedEvent.getId());
      return insertedEvent;
    } catch (IOException e) {
      throw new InsertingEventFailedException(e);
    }
  }

  private void checkExistenceOfMeetingRoom(final MeetingRoom room, final CalendarEvents calendarEvents) {
    if (isReservedMeetingRoom(room, calendarEvents)) {
      log.debug("already reservation exists : room = {}", room);
      throw new NotAvailableReserveEventException("해당 회의실은 이미 예약되었습니다.");
    }
  }

  private boolean isReservedMeetingRoom(MeetingRoom room, CalendarEvents eventsByTime) {
    return eventsByTime.findMeetingRooms()
            .stream()
            .anyMatch(meetingRoom -> meetingRoom.equals(room));
  }

  public Event createEventWith(final ReservationDateTime fetchingDate, final ReservationDetails reservationDetails) {
    EventDateTime startTime = fetchingDate.createEventDateTimeFromStartDateTime();
    EventDateTime endTime = fetchingDate.createEventDateTimeFromEndDateTime();

    return createEvent(reservationDetails, startTime, endTime);
  }

  private Event createEvent(final ReservationDetails reservationDetails, final EventDateTime startTime, final EventDateTime endTime) {
    String summary = createSummary(reservationDetails);

    return new Event()
            .setStart(startTime)
            .setEnd(endTime)
            .setSummary(summary);
  }

  private String createSummary(final ReservationDetails reservationDetails) {
    MeetingRoom meetingRoom = reservationDetails.getMeetingRoom();
    String booker = reservationDetails.getBooker();
    String description = reservationDetails.getDescription();

    return String.format("(%s) [%s] %s", meetingRoom.getName(), booker, description);
  }

  public Event changeEvent(final String eventId, final ReservationDateTime fetchingDate,
                           ReservationDetails reservationDetails, final CalendarId calendarId) {
    try {
      log.debug("update : event id = {}, fetching date = {}, details = {}", eventId, fetchingDate, reservationDetails);

      MeetingRoom room = reservationDetails.getMeetingRoom();
      CalendarEvents calendarEvents = findEvents(fetchingDate, calendarId);
      CalendarEvents calendarEventsExceptCurrentEvent = calendarEvents.excludeEventBy(eventId);
      checkExistenceOfMeetingRoom(room, calendarEventsExceptCurrentEvent);

      Event newEvent = createEventWith(fetchingDate, reservationDetails);

      Event updatedEvent = calendar.events()
              .update(calendarId.getId(), eventId, newEvent)
              .execute();
      log.debug("updated event : event id = {}", updatedEvent.getId());
      return updatedEvent;
    } catch (IOException e) {
      throw new UpdatingEventFailedException(e);
    }
  }

  public void cancelEvent(final String eventId, final CalendarId calendarId) {
    try {
      log.debug("cancel : event id = {}", eventId);

      calendar.events()
              .delete(calendarId.getId(), eventId)
              .execute();
    } catch (IOException e) {
      throw new DeletingEventFailedException(e);
    }
  }
}
