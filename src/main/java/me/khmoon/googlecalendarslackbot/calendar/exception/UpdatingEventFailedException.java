package me.khmoon.googlecalendarslackbot.calendar.exception;

public class UpdatingEventFailedException extends RuntimeException {
    public UpdatingEventFailedException(final Throwable e) {
        super(e);
    }
}
