package me.khmoon.googlecalendarslackbot.slack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.khmoon.googlecalendarslackbot.common.MeetingRoom;
import me.khmoon.googlecalendarslackbot.common.ReservationDetails;
import me.khmoon.googlecalendarslackbot.slack.EventType;
import me.khmoon.googlecalendarslackbot.slack.InitMenuType;
import me.khmoon.googlecalendarslackbot.slack.dto.request.BlockActionRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.EventCallbackRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.VerificationRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.CancelRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.ChangeRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.ReserveRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.RetrieveRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.response.changeandcancel.*;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalResponse;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalUpdateResponse;
import me.khmoon.googlecalendarslackbot.slack.dto.response.init.InitHomeTabResponseFactory;
import me.khmoon.googlecalendarslackbot.slack.dto.response.init.InitResponseFactory;
import me.khmoon.googlecalendarslackbot.slack.dto.response.reserve.ReserveAvailableMeetingResponseFactory;
import me.khmoon.googlecalendarslackbot.slack.dto.response.reserve.ReserveInputResponseFactory;
import me.khmoon.googlecalendarslackbot.slack.dto.response.reserve.ReserveResultResponseFactory;
import me.khmoon.googlecalendarslackbot.slack.dto.response.retrieve.RetrieveResultResponseFactory;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.DateTime;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.Reservation;
import me.khmoon.googlecalendarslackbot.slackcalendar.domain.Reservations;
import me.khmoon.googlecalendarslackbot.slackcalendar.service.SlackCalendarService;
import me.khmoon.googlecalendarslackbot.utils.BasicParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SlackService {

    private static final String BASE_URL = "https://slack.com/api";
    private static final String TOKEN = "Bearer " + System.getenv("BOT_TOKEN");

    private final SlackCalendarService slackCalendarService;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    public SlackService(SlackCalendarService slackCalendarService, ObjectMapper objectMapper) {
        this.slackCalendarService = slackCalendarService;
        this.objectMapper = objectMapper;
        this.webClient = initWebClient();
    }

    public String verify(VerificationRequest request) {
        return request.getChallenge();
    }

    public void showMenu(EventCallbackRequest request) {
        switch (EventType.of(request.getType())) {
            case APP_MENTION:
                send("/chat.postMessage", InitResponseFactory.of(request.getChannel()));
                break;
            case APP_HOME_OPENED:
                send("/views.publish", InitHomeTabResponseFactory.of(request.getUserId()));
        }
    }

    public void showModal(BlockActionRequest request) {
        if (request.getBlockId().equals("initial_block")) {
            send("/views.open", InitMenuType.of(request.getActionId()).apply(request.getTriggerId()));
            return;
        }
        if (request.getActionId().startsWith("request_reserve")) {
            send("/views.push", pushReserveModal(request));
            return;
        }
        if (request.getActionId().startsWith("request_change")) {
            send("/views.push", pushChangeModal(request));
            return;
        }
        if (request.getActionId().startsWith("request_cancel")) {
            send("/views.push", pushCancelModal(request));
        }
    }

    private ModalResponse pushReserveModal(BlockActionRequest request) {
        MeetingRoom meetingRoom = MeetingRoom.findByName(parseReservationId(request.getActionId()));
        List<String> tokens = BasicParser.parse(request.getPrivateMetadata(), "_");
        DateTime dateTime = DateTime.of(tokens.get(0), tokens.get(1), tokens.get(2));
        return ReserveInputResponseFactory.detail(request.getTriggerId(), dateTime, meetingRoom);
    }

    private ModalResponse pushChangeModal(BlockActionRequest request) {
        String reservationId = parseReservationId(request.getActionId());
        Reservation reservation = slackCalendarService.retrieveById(reservationId);
        return ChangeInputResponseFactory.of(request.getTriggerId(), reservation);
    }

    private String parseReservationId(String id) {
        String ID_REG = "_";
        int ID_INDEX = 2;
        return id.split(ID_REG)[ID_INDEX];
    }

    private ModalResponse pushCancelModal(BlockActionRequest request) {
        String reservationId = parseReservationId(request.getActionId());
        Reservation reservation = slackCalendarService.retrieveById(reservationId);
        return CancelConfirmResponseFactory.of(request.getTriggerId(), reservation);
    }

    public ModalUpdateResponse updateRetrieveResultModal(RetrieveRequest request) {
        DateTime retrieveRangeDateTime = DateTime.of(request.getDate()
            , generateLocalTime(request.getStartHour(), request.getStartMinute())
            , generateLocalTime(request.getEndHour(), request.getEndMinute()));
        Reservations reservations = slackCalendarService.retrieve(retrieveRangeDateTime);
        return RetrieveResultResponseFactory.of(retrieveRangeDateTime, reservations);
    }

    private LocalTime generateLocalTime(String hour, String minute) {
        return LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
    }

    public ModalUpdateResponse updateReserveAvailableMeetingRoomModal(ReserveRequest request) {
        DateTime dateTime = DateTime.of(request.getDate()
            , generateLocalTime(request.getStartHour(), request.getStartMinute())
            , generateLocalTime(request.getEndHour(), request.getEndMinute()));
        List<MeetingRoom> meetingRooms = slackCalendarService.retrieveAvailableMeetingRoom(dateTime);
        return ReserveAvailableMeetingResponseFactory.of(meetingRooms, dateTime);
    }

    public ModalUpdateResponse updateReserveResultModal(ReserveRequest request) {
        List<String> tokens = BasicParser.parse(request.getPrivateMetadata(), "_");
        DateTime dateTime = DateTime.of(tokens.get(0), tokens.get(1), tokens.get(2));
        ReservationDetails details = ReservationDetails.of(MeetingRoom.findByName(tokens.get(3)), request.getName(), request.getDescription());
        return ReserveResultResponseFactory.of(slackCalendarService.reserve(details, dateTime));
    }

    public ModalUpdateResponse updateChangeAndCancelCandidateModal(ChangeRequest request) {
        Reservations reservations = slackCalendarService.retrieve(request.getDate(), request.getName());
        return ChangeAndCancelCandidateResponseFactory.of(reservations);
    }

    public ModalUpdateResponse updateChangeResultModal(ReserveRequest request) {
        String reservationId = request.getPrivateMetadata();
        ReservationDetails details = ReservationDetails.of(
            MeetingRoom.findByName(request.getMeetingRoom()), request.getName(), request.getDescription()
        );
        DateTime dateTime = DateTime.of(request.getDate()
            , generateLocalTime(request.getStartHour(), request.getStartMinute())
            , generateLocalTime(request.getEndHour(), request.getEndMinute()));
        Reservation reservation = slackCalendarService.change(Reservation.of(reservationId, details, dateTime));
        return ChangeResultResponseFactory.of(reservation);
    }

    public ModalUpdateResponse updateCancelResultModal(CancelRequest request) {
        String reservationId = request.getPrivateMetadata();
        Reservation reservation = slackCalendarService.retrieveById(reservationId);
        slackCalendarService.cancel(reservation);
        return CancelResultResponseFactory.of(reservation);
    }

    private WebClient initWebClient() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(config ->
                    config.customCodecs().register(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON))
            ).build();
        return WebClient.builder()
            .exchangeStrategies(strategies)
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.AUTHORIZATION, TOKEN)
            .build();
    }

    private void send(String url, Object dto) {
        String response = Objects.requireNonNull(webClient.post()
                .uri(url)
                .body(BodyInserters.fromValue(dto))
                .exchange().block()).bodyToMono(String.class)
                .block();
        log.debug("WebClient Response: {}", response);
    }
}
