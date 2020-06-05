package me.khmoon.googlecalendarslackbot.slackcalendar.converter;

import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.common.ReservationDetails;
import me.khmoon.googlecalendarslackbot.utils.InvalidSummaryException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationDetailsConverterTest {

    @Test
    void transferReservationDetails() {
        String summary = " (G) [jason ben bran cooper] Bifan 스펙 확정 회의";
        String summary2 = " ( G)[jason ben bran cooper] ";
        String summary3 = " ( G)[ jason ben bran cooper] ";

        ReservationDetails reservationDetails = ReservationDetailsConverter.toReservationDetails(summary);
        ReservationDetails reservationDetails2 = ReservationDetailsConverter.toReservationDetails(summary2);
        ReservationDetails reservationDetails3 = ReservationDetailsConverter.toReservationDetails(summary3);

        assertEquals(reservationDetails, ReservationDetails.of(MeetingRoom.ROOM3, "jason ben bran cooper", "Bifan 스펙 확정 회의"));
        assertEquals(reservationDetails2, ReservationDetails.of(MeetingRoom.ROOM3, "jason ben bran cooper", ""));
        assertEquals(reservationDetails3, ReservationDetails.of(MeetingRoom.ROOM3, "jason ben bran cooper", ""));
    }

    private static String[] unFormatted = {
            "회의실/제목/목적"
            , "회의실//목적"
            , "Tall//"
            , "//"
            , "Tall,//제목/"
            , "Tall,제목,목적제목/"
            , "으아앙"
    };

    @ParameterizedTest
    @MethodSource("unFormattedSummaries")
    void isFormattedError(final String summary) {
        assertThrows(InvalidSummaryException.class, () -> ReservationDetailsConverter.toReservationDetails(summary));
    }

    private static Stream<String> unFormattedSummaries() {
        return Stream.of(unFormatted);
    }
}