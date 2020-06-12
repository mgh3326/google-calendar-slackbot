package me.khmoon.googlecalendarslackbot.slack.dto.response.init;


import me.khmoon.googlecalendarslackbot.slack.fragment.block.ActionsBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.Block;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.DividerBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.SectionBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.MrkdwnText;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.PlainText;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.ButtonElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.Element;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.ImageElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.view.HomeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InitHomeTabResponseFactory {
  public static InitialHomeTabResponse of(String userId) {
    List<Block> blocks = new ArrayList<>();

    blocks.add(new SectionBlock(
            new MrkdwnText("안녕하세요!\n캘린더 예약봇입니다."),
            new ImageElement("https://api.slack.com/img/blocks/bkb_template_images/notifications.png",
                    "calendar thumbnail")
    ));
    blocks.add(new DividerBlock());

    List<Element> elements = Arrays.asList(
            new ButtonElement(new PlainText(":spiral_calendar_pad: 전체 조회"), "retrieve"),
            new ButtonElement(new PlainText(":pushpin: 회의실 예약"), "reserve"),
            new ButtonElement(new PlainText(":scissors: 예약 변경/취소"), "change"));

    blocks.add(new ActionsBlock("initial_block", elements));

    return new InitialHomeTabResponse(userId, new HomeView(blocks));
  }
}
