package me.khmoon.googlecalendarslackbot.slackcalendar.service;

import com.google.api.services.calendar.model.Event;
import me.khmoon.googlecalendarslackbot.calendar.CalendarService;
import me.khmoon.googlecalendarslackbot.calendar.domain.CalendarEvents;
import me.khmoon.googlecalendarslackbot.calendar.domain.CalendarId;
import me.khmoon.googlecalendarslackbot.calendar.domain.ReservationDateTime;
import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.common.ReservationDetails;
import me.khmoon.googlecalendarslackbot.slackcalendar.converter.ReservationConverter;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.DateTime;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.Reservation;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.Reservations;
import me.khmoon.googlecalendarslackbot.slackcalendar.exception.InvalidEventException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-10
 */
@Service
public class SlackCalendarService {
    private final CalendarService calendarService;

    private final String calendarId = System.getenv("CALENDAR_ID");

    @Value("${calendar.summary.delimiter:/}")
    private String summaryDelimiter;

    public SlackCalendarService(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    public Reservations retrieve(DateTime dateTime) {
        CalendarEvents reservation = calendarService.findEvents(ReservationDateTime.of(dateTime.getFormattedDate(), dateTime.getFormattedStartTime(), dateTime.getFormattedEndTime())
            , CalendarId.from(calendarId));

        return Reservations.of(
            reservation.getEventsWithNotEmptySummary().stream()
                .map(event -> ReservationConverter.toReservation(event, summaryDelimiter))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(Reservation::getFormattedStartTime))
                .collect(Collectors.toList())
        );
    }

    public Reservations retrieve(String date, String booker) {
        CalendarEvents reservations = calendarService.findEvents(ReservationDateTime.of(date), CalendarId.from(calendarId));
        return Reservations.of(
            reservations.getEventsWithNotEmptySummary().stream()
                .map(event -> ReservationConverter.toReservation(event, summaryDelimiter))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(reservation -> reservation.isSameBooker(booker))
                .sorted(Comparator.comparing(Reservation::getFormattedStartTime))
                .collect(Collectors.toList())
        );
    }

    public Reservation retrieveById(String id) {
        Event event = calendarService.findEventById(id, CalendarId.from(calendarId))
            .orElseThrow(InvalidEventException::new);
        return ReservationConverter.toReservation(event, summaryDelimiter)
            .orElseThrow(InvalidEventException::new);
    }

    public List<MeetingRoom> retrieveAvailableMeetingRoom(DateTime dateTime) {
        Reservations reservations = retrieve(dateTime);
        return reservations.generateAvailableMeetingRooms();
    }

    public Reservation reserve(ReservationDetails details, DateTime dateTime) {
        Event event = calendarService.insertEvent(ReservationDateTime.of(dateTime.getFormattedDate(),
            dateTime.getFormattedStartTime(), dateTime.getFormattedEndTime())
            , details, CalendarId.from(calendarId));
        return ReservationConverter.toReservation(event, summaryDelimiter)
            .orElseThrow(InvalidEventException::new);
    }

    public Reservation change(Reservation preReservation) {
        Event event = calendarService.changeEvent(
            preReservation.getId(),
            ReservationDateTime.of(preReservation.getFormattedDate(), preReservation.getFormattedStartTime(), preReservation.getFormattedEndTime()),
            preReservation.getDetails(), CalendarId.from(calendarId)
        );
        return ReservationConverter.toReservation(event, summaryDelimiter)
            .orElseThrow(InvalidEventException::new);
    }

    public void cancel(Reservation reservation) {
        calendarService.cancelEvent(reservation.getId(), CalendarId.from(calendarId));
    }
}
