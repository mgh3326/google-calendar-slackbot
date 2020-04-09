package me.khmoon.googlecalendarslackbot.calendar.exception;

public class FetchingEventsFailedException extends RuntimeException {
  public FetchingEventsFailedException(final Throwable cause) {
    super(cause);
  }
}
