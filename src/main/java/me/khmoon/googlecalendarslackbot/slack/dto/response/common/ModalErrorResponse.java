package me.khmoon.googlecalendarslackbot.slack.dto.response.common;

import me.khmoon.googlecalendarslackbot.slack.fragment.error.Errors;

public class ModalErrorResponse {
    private final ModalActionType responseAction = ModalActionType.ERRORS;
    private Errors errors;

    public ModalErrorResponse() {
    }

    public ModalErrorResponse(Errors errors) {
        this.errors = errors;
    }

    public ModalActionType getResponseAction() {
        return responseAction;
    }

    public Errors getErrors() {
        return errors;
    }
}
