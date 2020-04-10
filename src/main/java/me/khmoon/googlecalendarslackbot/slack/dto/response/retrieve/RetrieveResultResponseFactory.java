package me.khmoon.googlecalendarslackbot.slack.dto.response.retrieve;


import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalSubmissionType;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalUpdateResponse;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.Block;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.ContextBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.DividerBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.SectionBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.MrkdwnText;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.PlainText;
import me.khmoon.googlecalendarslackbot.slack.fragment.view.ModalView;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.DateTime;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.Reservation;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.Reservations;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-10
 */
public class RetrieveResultResponseFactory {
    public static ModalUpdateResponse of(DateTime retrieveRangeDateTime, Reservations reservations) {
        return new ModalUpdateResponse(
            new ModalView(
                ModalSubmissionType.RETRIEVE_RESULT,
                new PlainText("조회하기"),
                new PlainText("확인"),
                generateBlocks(retrieveRangeDateTime, reservations)
            )
        );
    }

    private static List<Block> generateBlocks(DateTime retrieveRangeDateTime, Reservations reservations) {
        List<Block> blocks = new ArrayList<>();
        addTitleBlock(retrieveRangeDateTime, blocks);
        addRetrieveBlocks(reservations, blocks);
        return blocks;
    }

    private static void addTitleBlock(DateTime retrieveRangeDateTime, List<Block> blocks) {
        blocks.addAll(generateTitle(retrieveRangeDateTime.getFormattedDate(),
            retrieveRangeDateTime.getFormattedStartTime(), retrieveRangeDateTime.getFormattedEndTime()));
    }

    private static List<Block> generateTitle(String date, String startTime, String endTime) {
        return Collections.singletonList(
            new SectionBlock(
                new MrkdwnText(date + " " + startTime + "-" + endTime + " 회의실 예약 현황입니다.")
            )
        );
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
            reservation.getFormattedStartTime() + "-" + reservation.getFormattedEndTime()));
    }

    private static Block generateReservation(String booker, String purpose, String time) {
        return new SectionBlock(
            new MrkdwnText("*" + purpose + "*"),
            Arrays.asList(
                new PlainText(booker),
                new PlainText(time)
            )
        );
    }
}
