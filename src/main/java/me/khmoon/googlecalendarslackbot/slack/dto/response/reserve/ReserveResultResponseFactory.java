package me.khmoon.googlecalendarslackbot.slack.dto.response.reserve;

import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalSubmissionType;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalUpdateResponse;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.Block;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.DividerBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.block.SectionBlock;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.MrkdwnText;
import me.khmoon.googlecalendarslackbot.slack.fragment.composition.text.PlainText;
import me.khmoon.googlecalendarslackbot.slack.fragment.view.ModalView;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.Reservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-11
 */
public class ReserveResultResponseFactory {
    public static ModalUpdateResponse of(Reservation reserve) {
        return new ModalUpdateResponse(
            new ModalView(
                ModalSubmissionType.RESERVE_RESULT,
                new PlainText("예약하기"),
                new PlainText("확인"),
                generateBlocks(reserve),
                true
            )
        );
    }

    private static List<Block> generateBlocks(Reservation reservation) {
        List<Block> blocks = new ArrayList<>();
        addTitleBlock(blocks);
        addReserveBlocks(blocks, reservation);
        return blocks;
    }

    private static void addTitleBlock(List<Block> blocks) {
        blocks.add(new SectionBlock(new PlainText(":tada: 예약이 완료되었습니다!")));
        blocks.add(new DividerBlock());
    }

    private static void addReserveBlocks(List<Block> blocks, Reservation reservation) {
        blocks.add(generateReserve(reservation.getDescription(), reservation.getBooker(), reservation.getRoom().getName()
            , reservation.getFormattedDate(), reservation.getFormattedStartTime(), reservation.getFormattedEndTime()));
    }

    private static SectionBlock generateReserve(String description, String booker, String room,
                                                String date, String startTime, String endTime) {
        return new SectionBlock(
            new MrkdwnText("*" + room + " / " + description + "*"),
            Arrays.asList(
                new PlainText(booker),
                new PlainText(date + " " + startTime + "-" + endTime)
            )
        );
    }
}
