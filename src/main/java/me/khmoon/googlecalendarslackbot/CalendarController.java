package me.khmoon.googlecalendarslackbot;

import com.google.api.client.util.DateTime;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CalendarController {
  private final ApplicationProperties applicationProperties;
  private final Calendar calendar;

  @GetMapping("/find")
  public ResponseEntity<Events> findAllEvents(@RequestParam String fetchingDate) throws IOException {
    String calenderId = applicationProperties.getGoogle().getCalenderId();
    log.debug("fetching date : {}", fetchingDate);

    Calendar.Events eventsInCalendar = calendar.events();

    LocalDate localDate = LocalDate.parse(fetchingDate);
    LocalDateTime startOfDay = localDate.atStartOfDay();
    LocalDateTime endOfDay = LocalTime.MAX.atDate(localDate);

    Date startDate = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    Date endDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());

    Calendar.Events.List eventsList = eventsInCalendar.list(calenderId)
            .setTimeMin(new DateTime(startDate))
            .setTimeMax(new DateTime(endDate));
    Events fetchedSchedules = eventsList.execute();

    for (Event schedule : fetchedSchedules.getItems()) {
      log.info("만든이 - {}, 제목 - {}, 설명 - {}, 위치 - {}, 시작:{}, 끝:{}",
              schedule.getCreator(), schedule.getSummary(), schedule.getDescription(), schedule.getLocation()
              , schedule.getStart(), schedule.getEnd());
    }

    return new ResponseEntity<>(fetchedSchedules, HttpStatus.OK);
  }
}
