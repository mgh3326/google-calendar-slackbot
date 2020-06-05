package me.khmoon.googlecalendarslackbot.common;

import lombok.Getter;
import lombok.ToString;

import java.util.*;

@ToString
@Getter
public enum MeetingRoom {
  ROOM1("short"),
  ROOM2("tall"),
  ROOM3("grande"),
  ROOM4("venti"),
  NONE("");

  private String name;
  static final Map<String, String> namehashMap = new HashMap<>() {{
    put("s", MeetingRoom.ROOM1.name);
    put("t", MeetingRoom.ROOM2.name);
    put("g", MeetingRoom.ROOM3.name);
    put("v", MeetingRoom.ROOM4.name);
  }};

  MeetingRoom(String name) {
    this.name = name;
  }

  public static MeetingRoom findByName(String name) {
    return Arrays.stream(values())
            .filter(room -> room.name.equals(replaceToMeetingRoomName(name)))
            .findFirst()
            .orElse(NONE);
  }

  private static String replaceToMeetingRoomName(final String name) {
    return namehashMap.getOrDefault(name.toLowerCase(), name);
  }

  public static List<MeetingRoom> removeAll(List<MeetingRoom> meetingRooms) {
    List<MeetingRoom> allRooms = new ArrayList<>(Arrays.asList(values()));
    allRooms.removeAll(meetingRooms);
    validateOnlyWithNone(allRooms);
    return allRooms;
  }

  private static void validateOnlyWithNone(List<MeetingRoom> rooms) {
    rooms.remove(MeetingRoom.NONE);
    if (rooms.size() == 0) {
      throw new NotFoundAvailableMeetingRoomException();
    }
  }
}
