package me.khmoon.googlecalendarslackbot.calendar.exception;

public class DeletingEventFailedException extends RuntimeException {
    public DeletingEventFailedException(final Throwable cause) {
        super(cause);
    }
}
