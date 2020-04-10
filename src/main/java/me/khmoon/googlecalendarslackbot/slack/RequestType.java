package me.khmoon.googlecalendarslackbot.slack;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-03
 */
public enum RequestType {
    URL_VERIFICATION,
    EVENT_CALLBACK,
    BLOCK_ACTIONS,
    VIEW_SUBMISSION,
    APP_MENTION,
    APP_HOME_OPENED;

    public static RequestType of(String name) {
        return valueOf(name.toUpperCase());
    }
}
