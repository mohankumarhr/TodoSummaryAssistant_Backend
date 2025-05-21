package com.leucine.todosummaryassistant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.leucine.todosummaryassistant.model.Todo;
import com.leucine.todosummaryassistant.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SummaryService {

  @Autowired
  TodoRepository todoRepository;

  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${GEMINI_API_KEY}")
  private String geminiApiKey;

  @Value("${SLACK_WEBHOOK_URL}")
  private String slackWebhookUrl;

  public String generateSummary() {

    List<Todo> todos = todoRepository.findAll();
    String pending = todos.stream()
      .filter(t -> !t.isCompleted())
      .map(Todo::getTitle)
      .collect(Collectors.joining("\n- ", "Pending Todos:\n- ", ""));

    System.out.println("Pending todos:\n" + pending);

    try {
      Map<String, Object> textPart = Map.of("text", "Generate Summary for my pending todos" + pending);
      Map<String, Object> partWrapper = Map.of("parts", List.of(textPart));
      Map<String, Object> contentWrapper = Map.of("contents", List.of(partWrapper));

      ObjectMapper mapper = new ObjectMapper();
      String jsonBody = mapper.writeValueAsString(contentWrapper);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

      String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="+geminiApiKey;

      ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

      JsonNode root = mapper.readTree(response.getBody());
      JsonNode candidates = root.path("candidates");

      if (candidates.isArray() && !candidates.isEmpty()) {
        JsonNode content = candidates.get(0).path("content");
        JsonNode parts = content.path("parts");
        if (parts.isArray() && !parts.isEmpty()) {
          String summary = parts.get(0).path("text").asText();
          return summary;
        }
      }


      return "Error occurred during Generating Summery.";

    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred during Generating Summery.";
    }
  }


  public boolean sendSummaryToSlack(String summary) {
    try {
      HttpHeaders slackHeaders = new HttpHeaders();
      slackHeaders.setContentType(MediaType.APPLICATION_JSON);

      ObjectMapper mapper = new ObjectMapper();
      String slackBody = "{" + "\"text\": " + mapper.writeValueAsString(summary) + "}";
      HttpEntity<String> slackRequest = new HttpEntity<>(slackBody, slackHeaders);

      restTemplate.postForEntity(slackWebhookUrl, slackRequest, String.class);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Map<String, Object> summarizeAndSendToSlack() {
    Map<String, Object> response = new HashMap<>();
    try {
      String summary =  generateSummary();
      response.put("summary", summary);

      boolean slackStatus = sendSummaryToSlack(summary);
      if (slackStatus) {
        response.put("slackStatus", "Summary sent to Slack successfully.");
      } else {
        response.put("slackStatus", "Summary generated but failed to send to Slack.");
      }
//
    } catch (Exception e) {
      response.put("error", "Error occurred while generating or sending summary.");
      e.printStackTrace();
    }
    return response;
  }

}
