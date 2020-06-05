package me.khmoon.googlecalendarslackbot.calendar.domain;

import com.google.api.services.calendar.model.Event;
import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalendarEventsTest {
  private List<Event> events;

  @BeforeEach
  void setUp() {
    Event event1 = new Event().setSummary("(S) [티팍] 카카오 센터지원실").setId("event1");
    Event event2 = new Event().setSummary("(T) [jason ben bran cooper] Bifan 스펙 확정 회의").setId("event2");
    Event event3 = new Event().setSummary("(G) [robin] 서버팀 회식").setId("event3");

    events = Arrays.asList(event1, event2, event3);
  }

  @Test
  void findSummaries() {
    CalendarEvents calendarEvents = new CalendarEvents(events);

    List<String> summaries = calendarEvents.findSummaries();

    assertThat(summaries).isEqualTo(Arrays.asList("(S) [티팍] 카카오 센터지원실", "(T) [jason ben bran cooper] Bifan 스펙 확정 회의", "(G) [robin] 서버팀 회식"));
  }

  @Test
  void findMeetingRooms() {
    CalendarEvents calendarEvents = new CalendarEvents(events);

    List<MeetingRoom> meetingRooms = calendarEvents.findMeetingRooms();

    assertThat(meetingRooms).isEqualTo(Arrays.asList(MeetingRoom.ROOM1, MeetingRoom.ROOM2, MeetingRoom.ROOM3));
  }

  @Test
  void size() {
    assertThat(events.size()).isEqualTo(3);
  }

  @Test
  void getEvent() {
    CalendarEvents calendarEvents = new CalendarEvents(events);

    assertThat(calendarEvents.getEvent(0)).isEqualTo(events.get(0));
    assertThat(calendarEvents.getEvent(1)).isEqualTo(events.get(1));
    assertThat(calendarEvents.getEvent(2)).isEqualTo(events.get(2));
  }

  @Test
  void excludeEventBy() {
    CalendarEvents calendarEvents = new CalendarEvents(events);
    CalendarEvents filteredEvents = calendarEvents.excludeEventBy("event1");

    assertThat(filteredEvents.getEventsWithNotEmptySummary()).isEqualTo(Arrays.asList(events.get(1), events.get(2)));
  }
}