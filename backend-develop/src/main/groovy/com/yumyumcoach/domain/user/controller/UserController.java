package com.yumyumcoach.domain.user.controller;

import com.yumyumcoach.domain.title.dto.SelectMyTitleRequest;
import com.yumyumcoach.domain.user.dto.MyPageResponse;
import com.yumyumcoach.domain.title.dto.MyTitleResponse;
import com.yumyumcoach.domain.user.dto.UpdateMyBasicInfoRequest;
import com.yumyumcoach.domain.user.dto.UpdateMyHealthInfoRequest;
import com.yumyumcoach.domain.user.dto.UserProfileResponse;
import com.yumyumcoach.domain.user.service.UserService;
import com.yumyumcoach.global.common.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 내 마이페이지 조회
     */
    @GetMapping("/me/mypage")
    public MyPageResponse getMyPage() {
        String email = CurrentUser.email();
        return userService.getMyPage(email);
    }

    /**
     * 내 기본정보 수정
     * - (username / profileImageUrl / introduction)
     */
    @PatchMapping("/me/basic")
    public MyPageResponse.Basic updateMyBasicInfo(@RequestBody UpdateMyBasicInfoRequest request) {
        String email = CurrentUser.email();
        return userService.updateMyBasicInfo(email, request);
    }

    /**
     * 내 건강정보 수정
     */
    @PatchMapping("/me/health")
    public MyPageResponse.Health updateMyHealthInfo(@RequestBody UpdateMyHealthInfoRequest request) {
        String email = CurrentUser.email();
        return userService.updateMyHealthInfo(email, request);
    }

    /**
     * 내 대표뱃지 설정
     */
    @PatchMapping("/me/title")
    public MyTitleResponse selectMyTitle(@RequestBody SelectMyTitleRequest request) {
        String email = CurrentUser.email();
        return userService.selectMyTitle(email, request.getTitleId());
    }

    /**
     * 상대방 프로필 조회
     */
    @GetMapping("/{userId}")
    public UserProfileResponse getUserProfile(@PathVariable("userId") Long userId) {
        String viewerEmail = CurrentUser.email();
        return userService.getUserProfile(viewerEmail, userId);
    }
}

