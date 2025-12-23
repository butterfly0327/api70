package com.yumyumcoach.domain.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.key:${GMS_KEY:}}")
    private String apiKey;

    @Value("${gemini.api.url:https://gms.ssafy.io/gmsapi/generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent}")
    private String apiUrl;

    public String generateContent(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "프롬프트가 비어 있습니다.");
        }
        if (apiKey == null || apiKey.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Gemini API Key가 설정되지 않았습니다.");
        }

        Map<String, Object> body = new HashMap<>();
        body.put("contents", List.of(Map.of(
                "parts", List.of(Map.of("text", prompt))
        )));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String url = apiUrl + "?key=" + apiKey;

        try {
            String response = restTemplate.postForObject(url, request, String.class);
            return extractText(response);
        } catch (RestClientException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Gemini 호출에 실패했습니다.");
        }
    }

    private String extractText(String response) {
        if (response == null || response.isBlank()) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Gemini 응답이 비어 있습니다.");
        }
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
            if (textNode.isMissingNode() || textNode.asText(null) == null) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Gemini 응답 형식을 해석할 수 없습니다.");
            }
            return cleanText(textNode.asText());
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Gemini 응답 파싱에 실패했습니다.");
        }
    }

    private String cleanText(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            int firstNewLine = trimmed.indexOf('\n');
            int lastFence = trimmed.lastIndexOf("```");
            if (firstNewLine > 0 && lastFence > firstNewLine) {
                return trimmed.substring(firstNewLine + 1, lastFence).trim();
            }
        }
        return trimmed;
    }
}
