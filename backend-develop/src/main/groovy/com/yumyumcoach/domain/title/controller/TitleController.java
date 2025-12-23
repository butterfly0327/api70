package com.yumyumcoach.domain.title.controller;

import com.yumyumcoach.domain.title.dto.*;
import com.yumyumcoach.domain.title.service.TitleService;
import com.yumyumcoach.global.common.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me")
public class TitleController {

    private final TitleService titleService;

    @GetMapping("/title")
    public MyTitleResponse getMyCurrentTitle() {
        return titleService.getMyCurrentTitle(CurrentUser.email());
    }

    @PutMapping("/title")
    public MyTitleResponse updateMyCurrentTitle(@RequestBody UpdateMyTitleRequest request) {
        return titleService.updateMyCurrentTitle(CurrentUser.email(), request);
    }

    @GetMapping("/titles")
    public List<MyTitleItemResponse> getMyTitles() {
        return titleService.getMyTitles(CurrentUser.email());
    }
}
