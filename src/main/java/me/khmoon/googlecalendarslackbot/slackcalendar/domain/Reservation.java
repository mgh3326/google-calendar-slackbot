package me.khmoon.googlecalendarslackbot.slackcalendar.domain;


import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.common.ReservationDetails;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-11
 */
public class Reservation {
    private final String id;
    private final ReservationDetails details;
    private final DateTime time;

    private Reservation(final String id, final ReservationDetails details, final DateTime dateTime) {
        this.id = id;
        this.details = details;
        this.time = dateTime;
    }

    /**
     * @param date      yyyy-MM-dd
     * @param startTime HH:mm
     * @param endTime   HH:mm
     * @return
     */
    public static Reservation of(final String id, final MeetingRoom room, final String booker, final String description
        , final String date, final String startTime, final String endTime) {
        return new Reservation(id, ReservationDetails.of(room, booker, description)
            , DateTime.of(date, startTime, endTime));
    }

    public static Reservation of(final String id, final MeetingRoom room, final String booker, final String description
        , final LocalDate date, final LocalTime startTime, final LocalTime endTime) {
        return new Reservation(id, ReservationDetails.of(room, booker, description)
            , DateTime.of(date, startTime, endTime));
    }

    public static Reservation of(final String id, final ReservationDetails details, final DateTime time) {
        return new Reservation(id, details, time);
    }

    public static Reservation of (final String id, final ReservationDetails details
            , final String date, final String startTime, final String endTime) {
        return new Reservation(id, details, DateTime.of(date, startTime, endTime));
    }

    public boolean isSameBooker(String booker) {
        return details.isSameBooker(booker);
    }

    public String getId() {
        return id;
    }

    public MeetingRoom getRoom() {
        return details.getMeetingRoom();
    }

    public String getBooker() {
        return details.getBooker();
    }

    public String getDescription() {
        return details.getDescription();
    }

    public String getFormattedDate() {
        return time.getFormattedDate();
    }

    public String getFormattedStartTime() {
        return time.getFormattedStartTime();
    }

    public String getFormattedEndTime() {
        return time.getFormattedEndTime();
    }

    public ReservationDetails getDetails() {
        return details;
    }

    public int getStartHour() {
        return time.getStartHour();
    }

    public int getStartMinute() {
        return time.getStartMinute();
    }

    public int getEndHour() {
        return time.getEndHour();
    }

    public int getEndMinute() {
        return time.getEndMinute();
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id='" + id + '\'' +
            ", details=" + details +
            ", time=" + time +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(details, that.details)) return false;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
