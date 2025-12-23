package com.yumyumcoach.domain.ai.controller;

import com.yumyumcoach.domain.ai.dto.AiDetectionResponse;
import com.yumyumcoach.domain.ai.service.AiDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiDetectionController {
    private final AiDetectionService aiDetectionService;

    @PostMapping(value = "/detect", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AiDetectionResponse detect(@RequestPart("image") MultipartFile image) {
        return aiDetectionService.detect(image);
    }
}
