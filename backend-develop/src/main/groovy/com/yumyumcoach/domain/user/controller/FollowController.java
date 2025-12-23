package com.yumyumcoach.domain.user.controller;

import com.yumyumcoach.domain.user.dto.*;
import com.yumyumcoach.domain.user.service.FollowService;
import com.yumyumcoach.global.common.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class FollowController {

    private final FollowService followService;


    @GetMapping("/me/followings")
    public MyFollowingsResponse getMyFollowings() {
        String email = CurrentUser.email();
        return followService.getMyFollowings(email);
    }

    @GetMapping("/me/followers")
    public MyFollowersResponse getMyFollowers() {
        String email = CurrentUser.email();
        return followService.getMyFollowers(email);
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<FollowUserResponse> follow(@PathVariable("userId") Long userId) {
        String email = CurrentUser.email();
        FollowUserResponse res = followService.followUser(email, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/{userId}/follow")
    public UnfollowUserResponse unfollow(@PathVariable("userId") Long userId) {
        String email = CurrentUser.email();
        return followService.unfollowUser(email, userId);
    }
}
