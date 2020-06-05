package me.khmoon.googlecalendarslackbot.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicParserTest {

  @Test
  void parse() {
    String summary = "Short / 버디 / 프로젝트";

    assertEquals(BasicParser.parse(summary, "/"), Arrays.asList("Short", "버디", "프로젝트"));
  }

  @Test
  void meetingRoomParse() {
    String summary = "(V) [티팍 제이 알렉스] HB인베스트먼트";

    assertEquals(BasicParser.meetingRoomParse(summary), Arrays.asList("V", "티팍 제이 알렉스", "HB인베스트먼트"));
  }
}