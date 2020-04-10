package me.khmoon.googlecalendarslackbot.slack.dto.response.changeandcancel;

import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalSubmissionType;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalUpdateResponse;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.*;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.MrkdwnText;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.PlainText;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.ButtonElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.view.ModalView;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.Reservation;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.Reservations;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-11
 */
public class ChangeAndCancelCandidateResponseFactory {
    public static ModalUpdateResponse of(Reservations reservations) {
        return new ModalUpdateResponse(
            new ModalView(
                ModalSubmissionType.CHANGE_AND_CANCEL_CANDIDATE,
                new PlainText("변경/취소하기"),
                new PlainText("확인"),
                generateBlocks(reservations)
            )
        );
    }

    private static List<Block> generateBlocks(Reservations reservations) {
        List<Block> blocks = new ArrayList<>();
        addRetrieveBlocks(reservations, blocks);
        return blocks;
    }

    private static void addRetrieveBlocks(Reservations reservations, List<Block> blocks) {
        if (reservations.isEventEmpty()) {
            addEmptyBlock(blocks);
            return;
        }
        addReservationBlocks(reservations, blocks);
    }

    private static void addEmptyBlock(List<Block> blocks) {
        blocks.addAll(Collections.singletonList(
            new SectionBlock(
                new PlainText("예약이 없습니다!")
            )
        ));
    }

    private static void addReservationBlocks(Reservations reservations, List<Block> blocks) {
        TreeMap<MeetingRoom, List<Reservation>> roomReservation = reservations.stream()
            .collect(groupingBy(Reservation::getRoom, TreeMap::new, Collectors.toList()));
        addReservationBlocksByRoom(blocks, roomReservation);
    }

    private static void addReservationBlocksByRoom(List<Block> blocks, TreeMap<MeetingRoom, List<Reservation>> roomReservation) {
        roomReservation.forEach((key, value) -> blocks.addAll(generateReservations(key.getName(), value)));
    }

    private static List<Block> generateReservations(String room, List<Reservation> reservations) {
        List<Block> blocks = new ArrayList<>();
        addRoomBlock(room, blocks);
        reservations.forEach(event -> addReservationBlock(blocks, event));
        return blocks;
    }

    private static void addRoomBlock(String room, List<Block> blocks) {
        blocks.addAll(generateRoom(room));
    }

    private static List<Block> generateRoom(String room) {
        return Arrays.asList(
            new DividerBlock(),
            new ContextBlock(
                Collections.singletonList(
                    new MrkdwnText("*" + room + "*")
                )
            ),
            new DividerBlock()
        );
    }

    private static void addReservationBlock(List<Block> blocks, Reservation reservation) {
        blocks.add(generateReservation(reservation.getBooker(), reservation.getDescription(),
            reservation.getFormattedDate(), reservation.getFormattedStartTime() + "-" + reservation.getFormattedEndTime()));
        blocks.add(generateChangeButton(reservation.getId()));
    }

    private static Block generateReservation(String booker, String purpose, String date, String time) {
        return new SectionBlock(
            new MrkdwnText("*" + purpose + "*\n" + date + " " + time + " - " + booker)
        );
    }

    private static ActionsBlock generateChangeButton(String id) {
        return new ActionsBlock(
            "change_block_" + id,
            Arrays.asList(
                new ButtonElement(new PlainText("변경"), "request_change_" + id, "primary"),
                new ButtonElement(new PlainText("취소"), "request_cancel_" + id, "danger")
            )
        );
    }
}
