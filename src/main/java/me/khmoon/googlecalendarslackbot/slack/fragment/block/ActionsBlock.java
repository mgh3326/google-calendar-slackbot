package me.khmoon.googlecalendarslackbot.slack.fragment.block;

import me.khmoon.googlecalendarslackbot.slack.fragment.element.Element;

import java.util.List;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-04
 */
public class ActionsBlock extends Block {
    private String blockId;
    private List<Element> elements;

    public ActionsBlock() {
        super(BlockType.ACTIONS);
    }

    public ActionsBlock(String blockId, List<Element> elements) {
        super(BlockType.ACTIONS);
        this.blockId = blockId;
        this.elements = elements;
    }

    public List<Element> getElements() {
        return elements;
    }

    public String getBlockId() {
        return blockId;
    }
}
