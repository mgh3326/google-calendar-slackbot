package me.khmoon.googlecalendarslackbot.slack.fragment.element;

import me.khmoon.googlecalendarslackbot.slack.fragment.composition.Option;

import java.util.List;

public class OverflowElement extends Element {
    private String actionId;
    private List<Option> options;

    public OverflowElement() {
        super(ElementType.OVERFLOW);
    }

    public OverflowElement(String actionId, List<Option> options) {
        super(ElementType.OVERFLOW);
        this.actionId = actionId;
        this.options = options;
    }

    public String getActionId() {
        return actionId;
    }

    public List<Option> getOptions() {
        return options;
    }
}
