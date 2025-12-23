package com.yumyumcoach.domain.image.controller;

import com.yumyumcoach.domain.image.dto.PresignRequest;
import com.yumyumcoach.domain.image.dto.PresignResponse;
import com.yumyumcoach.domain.image.service.ImagePresignService;
import com.yumyumcoach.global.common.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImagePresignService imagePresignService;

    @PostMapping("/presign")
    public PresignResponse presign(@RequestBody PresignRequest req) {
        String email = CurrentUser.email();
        return imagePresignService.createPresign(req, email);
    }
}
