package me.khmoon.googlecalendarslackbot.slack.dto.response.init;


import me.khmoon.googlecalendarslackbot.slack.fragment.view.HomeView;

public class InitialHomeTabResponse {
    private String userId;
    private HomeView view;

    public InitialHomeTabResponse() {
    }

    public InitialHomeTabResponse(String userId, HomeView view) {
        this.userId = userId;
        this.view = view;
    }

    public String getUserId() {
        return userId;
    }

    public HomeView getView() {
        return view;
    }
}
