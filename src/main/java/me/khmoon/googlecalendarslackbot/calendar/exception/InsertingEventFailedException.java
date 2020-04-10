package me.khmoon.googlecalendarslackbot.calendar.exception;

public class InsertingEventFailedException extends RuntimeException {
    public InsertingEventFailedException(final Throwable e) {
        super(e);
    }
}
