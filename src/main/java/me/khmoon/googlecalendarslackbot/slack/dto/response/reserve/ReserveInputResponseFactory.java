package me.khmoon.googlecalendarslackbot.slack.dto.response.reserve;

import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.CommonResponseFactory;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalResponse;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalSubmissionType;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.DividerBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.InputBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.SectionBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.MrkdwnText;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.PlainText;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.DatepickerElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.PlainTextInputElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.view.ModalView;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.DateTime;

import java.util.Arrays;

public class ReserveInputResponseFactory {
    private static final String PREFIX_START = "start";
    private static final String PREFIX_END = "end";

    public static ModalResponse dateTime(String triggerId) {
        DatepickerElement datePicker = CommonResponseFactory.generateNowDatePicker("datepicker");

        ModalView modalView = new ModalView(
            ModalSubmissionType.RESERVE_DATETIME_INPUT,
            new PlainText("예약하기"),
            new PlainText("예약"),
            new PlainText("취소"),
            Arrays.asList(
                new InputBlock("datepicker_block", new PlainText("예약할 날짜를 선택하세요."), datePicker),
                new SectionBlock(new MrkdwnText("*시작 시간을 선택하세요.*")),
                CommonResponseFactory.generateHourPicker(PREFIX_START),
                CommonResponseFactory.generateMinutePickerWithInitValue(PREFIX_START, 0),
                new SectionBlock(new MrkdwnText("*종료 시간을 선택하세요.*")),
                CommonResponseFactory.generateHourPicker(PREFIX_END),
                CommonResponseFactory.generateMinutePickerWithInitValue(PREFIX_END, 0)
            )
        );
        return new ModalResponse(triggerId, modalView);
    }

    public static ModalResponse detail(String triggerId, DateTime dateTime, MeetingRoom meetingRoom) {
        String dateTimeWithMeetingRoom = dateTime.getFormattedDate() + "_" + dateTime.getFormattedStartTime()
            + "_" + dateTime.getFormattedEndTime() + "_" + meetingRoom.getName();
        ModalView modalView = new ModalView(
            ModalSubmissionType.RESERVE_DETAIL_INPUT,
            dateTimeWithMeetingRoom,
            new PlainText("예약하기"),
            new PlainText("예약"),
            new PlainText("취소"),
            Arrays.asList(
                new SectionBlock(new MrkdwnText("*" + dateTimeWithMeetingRoom.replace("_", " ") + "* 예약입니다.")),
                new DividerBlock(),
                new InputBlock("description_block", new PlainText("회의 제목을 입력하세요."),
                    new PlainTextInputElement("description", new PlainText("회의 제목"))),
                new InputBlock("name_block", new PlainText("예약자 이름을 입력하세요."),
                    new PlainTextInputElement("name", new PlainText("이름")))
            )
        );

        return new ModalResponse(triggerId, modalView);
    }
}
