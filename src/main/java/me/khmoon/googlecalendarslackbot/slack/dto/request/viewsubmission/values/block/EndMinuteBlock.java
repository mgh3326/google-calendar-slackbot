package me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.values.block;

public class EndMinuteBlock {
    private OptionWrapper endMinute;

    public EndMinuteBlock() {
    }

    public EndMinuteBlock(OptionWrapper endMinute) {
        this.endMinute = endMinute;
    }

    public OptionWrapper getEndMinute() {
        return endMinute;
    }
}
