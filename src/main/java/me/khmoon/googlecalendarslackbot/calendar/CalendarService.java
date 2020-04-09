package me.khmoon.googlecalendarslackbot.calendar;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import me.khmoon.googlecalendarslackbot.calendar.domain.ReservationDateTime;
import me.khmoon.googlecalendarslackbot.calendar.exception.FetchingEventsFailedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

  private final Calendar calendar;

  public List<Event> findReservation(final ReservationDateTime fetchingDate, final String calendarId) {
    try {
      Calendar.Events.List eventList = restrictEventsWithFetchingDate(fetchingDate, calendarId);
      Events results = eventList.execute();

      return results.getItems();
    } catch (IOException e) {
      throw new FetchingEventsFailedException(e);
    }
  }

  private Calendar.Events.List restrictEventsWithFetchingDate(final ReservationDateTime fetchingDate, final String calendarId) throws IOException {
    Calendar.Events eventsInCalendar = calendar.events();

    return eventsInCalendar.list(calendarId)
            .setTimeMin(fetchingDate.getStartDateTime())
            .setTimeMax(fetchingDate.getEndDateTime());
  }
}