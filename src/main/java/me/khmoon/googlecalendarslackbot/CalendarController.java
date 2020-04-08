package me.khmoon.googlecalendarslackbot;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.khmoon.googlecalendarslackbot.property.ApplicationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CalendarController {
  private final ApplicationProperties applicationProperties;
  private final Calendar calendar;


  @GetMapping("/find")
  public ResponseEntity<Events> findAllEvents() throws IOException {
    Events events = calendar.events().list(applicationProperties.getGoogle().getCalenderId()).execute();
    for (Event event : events.getItems()) {
      log.info("만든이 - {}, 제목 - {}, 설명 - {}, 위치 - {}, 시작:{}, 끝:{}",
              event.getCreator(), event.getSummary(), event.getDescription(), event.getLocation(), event.getStart(), event.getEnd());
    }

    return new ResponseEntity<>(events, HttpStatus.OK);
  }
}
