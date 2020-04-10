package me.khmoon.googlecalendarslackbot.slack.dto.response.changeandcancel;


import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.CommonResponseFactory;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalResponse;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalSubmissionType;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.InputBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.SectionBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.Option;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.MrkdwnText;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.PlainText;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.DatepickerElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.PlainTextInputElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.StaticSelectElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.view.ModalView;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.Reservation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeInputResponseFactory {
    private static final String PREFIX_START = "start";
    private static final String PREFIX_END = "end";

    public static ModalResponse of(String triggerId, Reservation reservation) {
        DatepickerElement datePicker = new DatepickerElement("datepicker", reservation.getFormattedDate());

        ModalView modalView = new ModalView(
            ModalSubmissionType.CHANGE_INPUT,
            reservation.getId(),
            new PlainText("변경하기"),
            new PlainText("변경"),
            new PlainText("취소"),
            Arrays.asList(
                new InputBlock("datepicker_block", new PlainText("예약할 날짜를 선택하세요."), datePicker),
                new SectionBlock(new MrkdwnText("*시작 시간을 선택하세요.*")),
                CommonResponseFactory.generateHourPickerWithInitValue(PREFIX_START, reservation.getStartHour()),
                CommonResponseFactory.generateMinutePickerWithInitValue(PREFIX_START, reservation.getStartMinute()),
                new SectionBlock(new MrkdwnText("*종료 시간을 선택하세요.*")),
                CommonResponseFactory.generateHourPickerWithInitValue(PREFIX_END, reservation.getEndHour()),
                CommonResponseFactory.generateMinutePickerWithInitValue(PREFIX_END, reservation.getEndMinute()),
                new InputBlock("meeting_room_block", new PlainText("회의실을 선택하세요."),
                    generateMeetingRoomSelectElement(reservation.getRoom().getName())),
                new InputBlock("description_block", new PlainText("회의 제목을 입력하세요."),
                    new PlainTextInputElement("description", new PlainText("회의 제목"), reservation.getDescription())),
                new InputBlock("name_block", new PlainText("예약자 이름을 입력하세요."),
                    new PlainTextInputElement("name", new PlainText("이름"), reservation.getBooker()))
            )
        );
        return new ModalResponse(triggerId, modalView);
    }

    private static StaticSelectElement generateMeetingRoomSelectElement(String initialMeetingRoom) {
        return new StaticSelectElement(
            new PlainText("회의실"),
            "meeting_room",
            new Option(new PlainText(initialMeetingRoom), initialMeetingRoom),
            generateMeetingRoomSelectOptions()
        );
    }

    private static List<Option> generateMeetingRoomSelectOptions() {
        return Arrays.stream(MeetingRoom.values())
            .filter(meetingRoom -> !meetingRoom.equals(MeetingRoom.NONE))
            .map(room -> new Option(new PlainText(room.getName()), room.getName()))
            .collect(Collectors.toList());
    }
}
