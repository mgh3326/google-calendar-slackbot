package me.khmoon.googlecalendarslackbot.slack.fragment.view;

import me.khmoon.googlecalendarslackbot.slack.fragment.block.Block;

import java.util.List;

public class HomeView {
    private final String type = "home";
    private List<Block> blocks;

    public HomeView() {
    }

    public HomeView(List<Block> blocks) {
        this.blocks = blocks;
    }

    public String getType() {
        return type;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
