package me.khmoon.googlecalendarslackbot.slack.fragment.block;


import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.Text;
import me.khmoon.googlecalendarslackbot.slack.fragment.element.Element;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-04
 */
public class InputBlock extends Block {
    private String blockId;
    private Text label;
    private Element element;

    public InputBlock() {
        super(BlockType.INPUT);
    }

    public InputBlock(String blockId, Text label, Element element) {
        super(BlockType.INPUT);
        this.blockId = blockId;
        this.label = label;
        this.element = element;
    }

    public Text getLabel() {
        return label;
    }

    public Element getElement() {
        return element;
    }

    public String getBlockId() {
        return blockId;
    }
}