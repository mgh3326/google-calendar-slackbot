package me.khmoon.googlecalendarslackbot.slack.dto.response.common;

public class ModalSubmissionResponse {
    private ModalActionType responseAction;

    public ModalSubmissionResponse() {
    }

    public ModalSubmissionResponse(ModalActionType responseAction) {
        this.responseAction = responseAction;
    }

    public ModalActionType getResponseAction() {
        return responseAction;
    }
}
