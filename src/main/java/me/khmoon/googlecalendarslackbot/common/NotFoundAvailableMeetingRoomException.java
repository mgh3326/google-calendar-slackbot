package me.khmoon.googlecalendarslackbot.common;

public class NotFoundAvailableMeetingRoomException extends RuntimeException {
    public NotFoundAvailableMeetingRoomException() {
        super("예약 가능한 회의실이 없습니다.");
    }
}
