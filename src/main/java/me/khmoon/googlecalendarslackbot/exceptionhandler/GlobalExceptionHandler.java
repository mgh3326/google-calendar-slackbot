package me.khmoon.googlecalendarslackbot.exceptionhandler;

import me.khmoon.googlecalendarslackbot.calendar.NotAvailableReserveEventException;
import me.khmoon.googlecalendarslackbot.calendar.domain.exception.InvalidDateTimeRangeException;
import me.khmoon.googlecalendarslackbot.common.NotFoundAvailableMeetingRoomException;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalErrorResponse;
import me.khmoon.googlecalendarslackbot.slack.fragment.error.DatePickerErrors;
import me.khmoon.googlecalendarslackbot.slack.fragment.error.DescriptionErrors;
import me.khmoon.googlecalendarslackbot.slack.fragment.error.TimeErrors;
import me.khmoon.googlecalendarslackbot.slackcalendar.exception.InvalidTimeRangeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-12
 */
@ControllerAdvice(basePackages = {"com.h3.reservation.controller"})
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(InvalidTimeRangeException.class)
    public ResponseEntity<ModalErrorResponse> handleInvalidTimeRangeException(InvalidTimeRangeException e) {
        return ResponseEntity.ok(new ModalErrorResponse(new TimeErrors(e.getMessage())));
    }

    @ResponseBody
    @ExceptionHandler(InvalidDateTimeRangeException.class)
    public ResponseEntity handleInvalidDateTimeRangeException(InvalidDateTimeRangeException e) {
        return ResponseEntity.ok(new ModalErrorResponse(new DatePickerErrors(e.getMessage())));
    }

    @ResponseBody
    @ExceptionHandler(NotAvailableReserveEventException.class)
    public ResponseEntity NotAvailableReserveEventException(NotAvailableReserveEventException e) {
        return ResponseEntity.ok(new ModalErrorResponse(new DescriptionErrors(e.getMessage())));
    }

    @ResponseBody
    @ExceptionHandler(NotFoundAvailableMeetingRoomException.class)
    public ResponseEntity NotFoundAvailableMeetingRoomException(NotFoundAvailableMeetingRoomException e) {
        return ResponseEntity.ok(new ModalErrorResponse(new DatePickerErrors(e.getMessage())));
    }
}
