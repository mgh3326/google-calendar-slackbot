package me.khmoon.googlecalendarslackbot.slackcalendar.domain;

import java.util.Objects;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-11
 */
public class Component {
    private final String room;
    private final String booker;
    private final String purpose;

    private Component(final String room, final String booker, final String purpose) {
        this.room = room;
        this.booker = booker;
        this.purpose = purpose;
    }

    public static Component of(final String room, final String booker, final String purpose) {
        return new Component(room, booker, purpose);
    }

    public String getRoom() {
        return room;
    }

    public String getBooker() {
        return booker;
    }

    public String getPurpose() {
        return purpose;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Component that = (Component) o;

        if (!Objects.equals(room, that.room)) return false;
        if (!Objects.equals(booker, that.booker)) return false;
        return Objects.equals(purpose, that.purpose);
    }

    @Override
    public int hashCode() {
        int result = room != null ? room.hashCode() : 0;
        result = 31 * result + (booker != null ? booker.hashCode() : 0);
        result = 31 * result + (purpose != null ? purpose.hashCode() : 0);
        return result;
    }
}
