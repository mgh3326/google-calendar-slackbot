package me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.values.block;

public class MeetingRoomBlock {
    private OptionWrapper meetingRoom;

    public MeetingRoomBlock() {
    }

    public MeetingRoomBlock(OptionWrapper meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public OptionWrapper getMeetingRoom() {
        return meetingRoom;
    }
}
