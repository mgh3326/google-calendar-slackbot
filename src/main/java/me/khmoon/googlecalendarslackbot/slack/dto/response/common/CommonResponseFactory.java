package me.khmoon.googlecalendarslackbot.slack.dto.response.common;

import me.khmoon.googlecalendarslackbot.slack.fragment.block.InputBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.Option;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.PlainText;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.DatepickerElement;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.StaticSelectElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommonResponseFactory {
    private static final String INIT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String HOUR = "시";
    private static final String MINUTE = "분";
    private static final String SUFFIX_HOUR_BLOCK = "_hour_block";
    private static final String SUFFIX_HOUR = "_hour";
    private static final String SUFFIX_MINUTE_BLOCK = "_minute_block";
    private static final String SUFFIX_MINUTE = "_minute";
    private static final int MIN_HOUR = 10;
    private static final int MAX_HOUR = 21;
    private static final int MIN_MINUTE = 0;
    private static final int MAX_MINUTE = 45;
    private static final int MINUTE_INTERVAL = 15;

    public static DatepickerElement generateDatePickerWithInitValue(String actionId, String initDate) {
        return new DatepickerElement(actionId, initDate);
    }
    public static DatepickerElement generateNowDatePicker(String actionId) {
        return generateDatePickerWithInitValue(actionId, generateNowDate());
    }

    private static String generateNowDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(INIT_DATE_PATTERN));
    }

    public static InputBlock generateHourPickerWithInitValue(String prefix, int initialHour) {
        return new InputBlock(
            prefix + SUFFIX_HOUR_BLOCK,
            new PlainText("시간을 선택하세요."),
            new StaticSelectElement(
                new PlainText(HOUR),
                prefix + SUFFIX_HOUR,
                new Option(new PlainText(initialHour + HOUR), String.valueOf(initialHour)),
                generateHourSelect()
            )
        );
    }

    public static InputBlock generateHourPicker(String prefix) {
        return new InputBlock(
            prefix + SUFFIX_HOUR_BLOCK,
            new PlainText("시간을 선택하세요."),
            new StaticSelectElement(
                new PlainText(HOUR),
                prefix + SUFFIX_HOUR,
                generateHourSelect()
            )
        );
    }

    public static InputBlock generateMinutePickerWithInitValue(String prefix, int initialMinute) {
        return new InputBlock(
            prefix + SUFFIX_MINUTE_BLOCK,
            new PlainText("분을 선택하세요."),
            new StaticSelectElement(
                new PlainText(MINUTE),
                prefix + SUFFIX_MINUTE,
                new Option(new PlainText(initialMinute + MINUTE), String.valueOf(initialMinute)),
                generateMinuteSelect()
            )
        );
    }

    public static InputBlock generateMinutePicker(String prefix) {
        return new InputBlock(
            prefix + SUFFIX_MINUTE_BLOCK,
            new PlainText("분을 선택하세요."),
            new StaticSelectElement(
                new PlainText(MINUTE),
                prefix + SUFFIX_MINUTE,
                generateMinuteSelect()
            )
        );
    }

    private static List<Option> generateHourSelect() {
        List<Option> options = new ArrayList<>();
        for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
            options.add(new Option(new PlainText(i + HOUR), String.valueOf(i)));
        }
        return options;
    }

    private static List<Option> generateMinuteSelect() {
        List<Option> options = new ArrayList<>();
        for (int i = MIN_MINUTE; i <= MAX_MINUTE; i += MINUTE_INTERVAL) {
            options.add(new Option(new PlainText(i + MINUTE), String.valueOf(i)));
        }
        return options;
    }
}
