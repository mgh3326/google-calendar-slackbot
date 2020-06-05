package me.khmoon.googlecalendarslackbot.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class BasicParser {

  public static List<String> meetingRoomParse(final String text) {
    String room = Optional.ofNullable(StringUtils.substringBetween(text, "(", ")")).orElse("");
    String booker = Optional.ofNullable(StringUtils.substringBetween(text, "[", "]")).orElse("");
    int purposeStartIndex = 0;
    if (!text.contains("]")) {
      if (text.contains(")")) {
        purposeStartIndex = text.indexOf(")") + 1;
      }
    } else {
      purposeStartIndex = text.indexOf("]") + 1;
    }
    String purpose = text.substring(purposeStartIndex);
    return Stream.of(room, booker, purpose)
            .map(s -> {
              if (s != null)
              {
              return s.trim();
              }
              else
                return s;
            })
            .collect(Collectors.toList());
  }

  public static List<String> parse(final String text, final String delimiter) {
    return Arrays.stream(text.split(delimiter))
            .map(String::trim)
            .collect(Collectors.toList());
  }
}
