package me.khmoon.googlecalendarslackbot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.khmoon.googlecalendarslackbot.slack.RequestType;
import me.khmoon.googlecalendarslackbot.slack.dto.request.BlockActionRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.EventCallbackRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.VerificationRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.CancelRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.ChangeRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.ReserveRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.request.viewsubmission.RetrieveRequest;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalClearResponse;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalSubmissionResponse;
import me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalSubmissionType;
import me.khmoon.googlecalendarslackbot.slack.service.SlackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static me.khmoon.googlecalendarslackbot.slack.RequestType.*;
import static me.khmoon.googlecalendarslackbot.slack.dto.response.common.ModalSubmissionType.*;

/**
 * @author heebg
 * @version 1.0
 * @date 2019-12-02
 */
@RestController
public class BotController {
    private final ObjectMapper objectMapper;
    private final SlackService service;

    public BotController(ObjectMapper objectMapper, SlackService service) {
        this.objectMapper = objectMapper;
        this.service = service;
    }

    @PostMapping("/slack/action")
    public ResponseEntity<String> action(@RequestBody JsonNode reqJson) throws JsonProcessingException {
        switch (RequestType.of(reqJson.get("type").asText())) {
            case URL_VERIFICATION:
                return ResponseEntity.ok(service.verify(jsonToDto(reqJson, VerificationRequest.class)));
            case EVENT_CALLBACK:
                service.showMenu(jsonToDto(reqJson, EventCallbackRequest.class));
                return ResponseEntity.ok().build();
            default:
                return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/slack/interaction")
    public ResponseEntity<?> interaction(@RequestParam Map<String, String> req) throws IOException {
        JsonNode reqJson = objectMapper.readTree(req.get("payload"));
        switch (RequestType.of(reqJson.get("type").asText())) {
            case BLOCK_ACTIONS:
                service.showModal(jsonToDto(reqJson, BlockActionRequest.class));
                return ResponseEntity.ok().build();
            case VIEW_SUBMISSION:
                return ResponseEntity.ok(Objects.requireNonNull(generateModalSubmissionResponse(reqJson)));
            default:
                return ResponseEntity.badRequest().build();
        }
    }

    private ModalSubmissionResponse generateModalSubmissionResponse(JsonNode reqJson) throws IOException {
        switch (ModalSubmissionType.of(reqJson.get("view").get("callback_id").asText())) {
            case RETRIEVE_INPUT:
                return service.updateRetrieveResultModal(jsonToDto(reqJson, RetrieveRequest.class));
            case RESERVE_DATETIME_INPUT:
                return service.updateReserveAvailableMeetingRoomModal(jsonToDto(reqJson, ReserveRequest.class));
            case RESERVE_DETAIL_INPUT:
                return service.updateReserveResultModal(jsonToDto(reqJson, ReserveRequest.class));
            case CHANGE_AND_CANCEL_INPUT:
                return service.updateChangeAndCancelCandidateModal(jsonToDto(reqJson, ChangeRequest.class));
            case CHANGE_INPUT:
                return service.updateChangeResultModal(jsonToDto(reqJson, ReserveRequest.class));
            case CANCEL_CONFIRM:
                return service.updateCancelResultModal(jsonToDto(reqJson, CancelRequest.class));
        }
        return new ModalClearResponse();
    }

    private <T> T jsonToDto(JsonNode json, Class<T> type) throws JsonProcessingException {
        return objectMapper.treeToValue(json, type);
    }
}
