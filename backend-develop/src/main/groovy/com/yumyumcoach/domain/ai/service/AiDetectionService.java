package com.yumyumcoach.domain.ai.service;

import com.yumyumcoach.domain.ai.dto.AiDetectionResponse;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AiDetectionService {
    @Value("${ai.server.url:http://localhost:8000}")
    private String aiServerUrl;

    @Value("${ai.server.detect-path:/api/v1/detect}")
    private String detectPath;

    private final RestTemplate restTemplate = new RestTemplate();

    public AiDetectionResponse detect(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "이미지 파일을 제공해주세요.");
        }

        byte[] bytes = toBytes(image);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", asFileResource(image, bytes));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String url = buildDetectUrl();

        try {
            ResponseEntity<AiDetectionResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    AiDetectionResponse.class
            );

            if (response.getBody() == null) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "AI 서버 응답을 파싱할 수 없습니다.");
            }

            return response.getBody();
        } catch (RestClientException ex) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "AI 서버와 통신에 실패했습니다.");
        }
    }

    private ByteArrayResource asFileResource(MultipartFile image, byte[] bytes) {
        return new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        };
    }

    private byte[] toBytes(MultipartFile image) {
        try {
            return image.getBytes();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "이미지 파일을 읽을 수 없습니다.");
        }
    }

    private String buildDetectUrl() {
        if (aiServerUrl.endsWith("/")) {
            return aiServerUrl.substring(0, aiServerUrl.length() - 1) + detectPath;
        }
        return aiServerUrl + detectPath;
    }
}
