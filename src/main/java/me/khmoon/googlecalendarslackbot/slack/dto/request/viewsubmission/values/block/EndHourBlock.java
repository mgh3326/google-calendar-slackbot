package me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.values.block;

public class EndHourBlock {
    private OptionWrapper endHour;

    public EndHourBlock() {
    }

    public EndHourBlock(OptionWrapper endHour) {
        this.endHour = endHour;
    }

    public OptionWrapper getEndHour() {
        return endHour;
    }
}
