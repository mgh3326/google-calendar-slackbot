package me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission;

import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.values.RetrieveValues;

public class RetrieveView {
    class State {
        private RetrieveValues values;

        public State() {
        }

        public State(RetrieveValues values) {
            this.values = values;
        }

        public RetrieveValues getValues() {
            return values;
        }
    }

    private String callbackId;
    private State state;

    public RetrieveView() {
    }

    public RetrieveView(String callbackId, State state) {
        this.callbackId = callbackId;
        this.state = state;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public State getState() {
        return state;
    }

    public RetrieveValues getValues() {
        return state.getValues();
    }
}
