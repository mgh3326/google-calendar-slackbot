package me.khmoon.googlecalendarslackbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestService {

  private final RestTemplate restTemplate;

  public RestService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public boolean sendText(String text) {
    String url = "https://hooks.slack.com/services/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";

    // create headers
    HttpHeaders headers = new HttpHeaders();
    // set `content-type` header
    headers.setContentType(MediaType.APPLICATION_JSON);
    // set `accept` header
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    // create a map for post parameters
    Map<String, Object> map = new HashMap<>();
    map.put("text", text);

    // build the request
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

    // send POST request
    ResponseEntity<String> response = this.restTemplate.postForEntity(url, entity, String.class);

    // check response status code
    if (response.getStatusCode() == HttpStatus.OK) {
      return true;
    } else {
      sendError();
      return false;
    }
  }

  public void sendError() {
    String url = "https://hooks.slack.com/services/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";

    // create headers
    HttpHeaders headers = new HttpHeaders();
    // set `content-type` header
    headers.setContentType(MediaType.APPLICATION_JSON);
    // set `accept` header
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    // create a map for post parameters
    Map<String, Object> map = new HashMap<>();
    map.put("text", "Incoming Webhooks 에러 발생");

    // build the request
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

    // send POST request
    this.restTemplate.postForEntity(url, entity, String.class);
  }

}