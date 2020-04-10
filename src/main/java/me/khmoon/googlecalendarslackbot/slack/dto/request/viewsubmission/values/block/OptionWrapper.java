package me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.values.block;

import me.khmoon.googlecalendarslackbot.slack.fragment.composition.Option;

public class OptionWrapper {
    private Option selectedOption;

    public OptionWrapper() {
    }

    public OptionWrapper(Option selectedOption) {
        this.selectedOption = selectedOption;
    }

    public Option getSelectedOption() {
        return selectedOption;
    }
}
