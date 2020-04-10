package me.khmoon.googlecalendarslackbot.slack.dto.response.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-12
 */
public enum ModalActionType {
    UPDATE("update"),
    CLEAR("clear"),
    ERRORS("errors");

    @JsonValue
    private String type;

    ModalActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
