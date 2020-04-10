package me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.values;

import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.values.block.DatepickerBlock;
import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.values.block.NameBlock;

public class ChangeValues {
    private DatepickerBlock datepickerBlock;
    private NameBlock nameBlock;

    public ChangeValues() {
    }

    public ChangeValues(DatepickerBlock datepickerBlock, NameBlock nameBlock) {
        this.datepickerBlock = datepickerBlock;
        this.nameBlock = nameBlock;
    }

    public DatepickerBlock getDatepickerBlock() {
        return datepickerBlock;
    }

    public NameBlock getNameBlock() {
        return nameBlock;
    }
}
