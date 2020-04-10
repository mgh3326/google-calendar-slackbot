package me.khmoon.googlecalendarslackbot.calendar;

public class NotAvailableReserveEventException extends RuntimeException {
    public NotAvailableReserveEventException(String message) {
        super(message);
    }
}
