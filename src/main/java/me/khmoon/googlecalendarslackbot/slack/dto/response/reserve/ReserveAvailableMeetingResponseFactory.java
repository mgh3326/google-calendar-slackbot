package me.khmoon.googlecalendarslackbot.slack.dto.response.reserve;

import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalSubmissionType;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalUpdateResponse;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.Block;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.DividerBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.SectionBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.MrkdwnText;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.PlainText;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.ButtonElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.view.ModalView;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReserveAvailableMeetingResponseFactory {
    public static ModalUpdateResponse of(List<MeetingRoom> meetingRooms, DateTime dateTime) {
        return new ModalUpdateResponse(
            new ModalView(
                ModalSubmissionType.RESERVE_AVAILABLE_MEETINGROOM,
                dateTime.getFormattedDate() + "_" + dateTime.getFormattedStartTime() + "_" + dateTime.getFormattedEndTime(),
                new PlainText("예약하기"),
                new PlainText("확인"),
                generateMeetingRoomBlocks(meetingRooms, dateTime)
            )
        );
    }

    private static List<Block> generateMeetingRoomBlocks(List<MeetingRoom> meetingRooms, DateTime dateTime) {
        List<Block> blocks = new ArrayList<>();
        addTitleBlock(blocks, dateTime);
        addAvailableMeetingRooms(blocks, meetingRooms);
        return blocks;
    }

    private static void addTitleBlock(List<Block> blocks, DateTime dateTime) {
        String date = dateTime.getFormattedDate() + " " + dateTime.getFormattedStartTime() + "-" + dateTime.getFormattedEndTime();
        blocks.add(new SectionBlock(new PlainText(date + "에 예약 가능한 회의실은 다음과 같습니다.")));
        blocks.add(new DividerBlock());
    }

    private static void addAvailableMeetingRooms(List<Block> blocks, List<MeetingRoom> meetingRooms) {
        blocks.addAll(generateAvailableMeetingRooms(meetingRooms));
    }

    private static List<Block> generateAvailableMeetingRooms(List<MeetingRoom> meetingRooms) {
        return meetingRooms.stream()
            .map(ReserveAvailableMeetingResponseFactory::generateAvailableMeetingRoom)
            .collect(Collectors.toList());
    }

    private static SectionBlock generateAvailableMeetingRoom(MeetingRoom meetingRoom) {
        return new SectionBlock(new MrkdwnText("*" + meetingRoom.getName() + "*"),
            new ButtonElement(new PlainText("선택"), "request_reserve_" + meetingRoom.getName()));
    }

}
