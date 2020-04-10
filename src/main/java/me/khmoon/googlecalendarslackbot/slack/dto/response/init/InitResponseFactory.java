package me.khmoon.googlecalendarslackbot.slack.dto.response.init;

import me.khmoon.googlecalendarslackbot.slack.fragment.block.ActionsBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.PlainText;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.ButtonElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.Element;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InitResponseFactory {
    public static InitialResponse of(String channel) {
        List<Element> elements = Arrays.asList(
            new ButtonElement(new PlainText(":spiral_calendar_pad: 전체 조회"), "retrieve"),
            new ButtonElement(new PlainText(":pushpin: 회의실 예약"), "reserve"),
            new ButtonElement(new PlainText(":scissors: 예약 변경/취소"), "change"));

        List<ActionsBlock> blocks = Collections.singletonList(new ActionsBlock("initial_block", elements));

        return new InitialResponse(channel, blocks);
    }
}
