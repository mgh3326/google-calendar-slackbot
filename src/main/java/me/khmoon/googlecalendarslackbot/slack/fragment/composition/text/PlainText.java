package me.khmoon.googlecalendarslackbot.slack.fragment.composition.text;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-04
 */
public class PlainText extends Text {
    public PlainText() {
    }

    public PlainText(String text) {
        super(TextType.PLAIN_TEXT, text);
    }
}
