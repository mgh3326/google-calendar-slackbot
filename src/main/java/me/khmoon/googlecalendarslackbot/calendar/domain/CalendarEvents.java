package me.khmoon.googlecalendarslackbot.calendar.domain;

import com.google.api.services.calendar.model.Event;
import lombok.RequiredArgsConstructor;
import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.utils.BasicParser;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CalendarEvents {

    private static final int INDEX_OF_MEETING_ROOM = 0;

    private final List<Event> events;

    public List<String> findSummaries() {
        return events.stream()
            .map(Event::getSummary)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
    }

    public List<MeetingRoom> findMeetingRooms(final String summaryDelimiter) {
        return findSummaries().stream()
            .map(summary -> BasicParser.parse(summary, summaryDelimiter))
            .map(tokens -> tokens.get(INDEX_OF_MEETING_ROOM))
            .map(roomName -> roomName.replace(" ", ""))
            .map(MeetingRoom::findByName)
            .collect(Collectors.toList());
    }

    public CalendarEvents excludeEventBy(final String eventId) {
        List<Event> filteredEvents = this.events.stream()
            .filter(e -> !e.getId().equals(eventId))
            .collect(Collectors.toList());

        return new CalendarEvents(filteredEvents);
    }

    public List<Event> getEventsWithNotEmptySummary() {
        return events.stream()
            .filter(event -> StringUtils.isNotBlank(event.getSummary()))
            .collect(Collectors.toList());
    }

    public int size() {
        return events.size();
    }

    public Event getEvent(int index) {
        return events.get(index);
    }
}
