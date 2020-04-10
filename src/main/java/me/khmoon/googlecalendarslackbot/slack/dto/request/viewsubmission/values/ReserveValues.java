package me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.values;


import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.values.block.*;

public class ReserveValues {
    private StartHourBlock startHourBlock;
    private StartMinuteBlock startMinuteBlock;
    private EndHourBlock endHourBlock;
    private EndMinuteBlock endMinuteBlock;
    private DatepickerBlock datepickerBlock;
    private MeetingRoomBlock meetingRoomBlock;
    private DescriptionBlock descriptionBlock;
    private NameBlock nameBlock;

    public ReserveValues() {
    }

    public ReserveValues(StartHourBlock startHourBlock, StartMinuteBlock startMinuteBlock, EndHourBlock endHourBlock,
                         EndMinuteBlock endMinuteBlock, DatepickerBlock datepickerBlock,
                         MeetingRoomBlock meetingRoomBlock, DescriptionBlock descriptionBlock, NameBlock nameBlock) {
        this.startHourBlock = startHourBlock;
        this.startMinuteBlock = startMinuteBlock;
        this.endHourBlock = endHourBlock;
        this.endMinuteBlock = endMinuteBlock;
        this.datepickerBlock = datepickerBlock;
        this.meetingRoomBlock = meetingRoomBlock;
        this.descriptionBlock = descriptionBlock;
        this.nameBlock = nameBlock;
    }

    public StartHourBlock getStartHourBlock() {
        return startHourBlock;
    }

    public StartMinuteBlock getStartMinuteBlock() {
        return startMinuteBlock;
    }

    public EndHourBlock getEndHourBlock() {
        return endHourBlock;
    }

    public EndMinuteBlock getEndMinuteBlock() {
        return endMinuteBlock;
    }

    public DatepickerBlock getDatepickerBlock() {
        return datepickerBlock;
    }

    public MeetingRoomBlock getMeetingRoomBlock() {
        return meetingRoomBlock;
    }

    public DescriptionBlock getDescriptionBlock() {
        return descriptionBlock;
    }

    public NameBlock getNameBlock() {
        return nameBlock;
    }
}
