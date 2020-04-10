package me.khmoon.googlecalendarslackbot.slack;

import me.khmoon.googlecalendarslackbot.slack.dto.response.changeandcancel.ChangeAndCancelInputResponseFactory;
import me.khmoon.googlecalendarslackbot.slack.dto.response.reserve.ReserveInputResponseFactory;
import me.khmoon.googlecalendarslackbot.slack.dto.response.retrieve.RetrieveInputResponseFactory;

import java.util.function.Function;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-04
 */
public enum InitMenuType {
    RETRIEVE(RetrieveInputResponseFactory::of),
    RESERVE(ReserveInputResponseFactory::dateTime),
    CHANGE(ChangeAndCancelInputResponseFactory::of);

    private Function<String, Object> function;

    public static InitMenuType of(String name) {
        return valueOf(name.toUpperCase());
    }

    InitMenuType(Function<String, Object> function) {
        this.function = function;
    }

    public Object apply(String id) {
        return function.apply(id);
    }
}
