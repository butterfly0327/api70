package com.yumyumcoach.domain.auth.controller;

import com.yumyumcoach.domain.auth.dto.*;
import com.yumyumcoach.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> login(@Valid @RequestBody SignInRequest request) {

        SignInResponse response = authService.SignIn(request);

        return ResponseEntity.ok(response);
    }

    // 로그아웃
    @DeleteMapping("/sign-out")
    public ResponseEntity<SignOutResponse> logout(@AuthenticationPrincipal String email,
                                                  @RequestBody SignOutRequest request) {
        SignOutResponse response = authService.SingOut(email, request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    // 이메일 중복 검사
    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmail(@RequestParam("email") String email) {
        boolean available = authService.isEmailAvailable(email);
        return ResponseEntity.ok(new EmailCheckResponse(email, available));
    }

    // 닉네임 중복 검사
    @GetMapping("/check-username")
    public ResponseEntity<UsernameCheckResponse> checkUsername(@RequestParam("username") String username) {
        boolean available = authService.isUsernameAvailable(username);
        return ResponseEntity.ok(new UsernameCheckResponse(username, available));
    }

    // 회원 가입
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(@AuthenticationPrincipal String email,
                                                     @Valid @RequestBody WithdrawRequest request) {
        WithdrawResponse response = authService.withdraw(email, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        RefreshResponse response = authService.refreshTokens(request);
        return ResponseEntity.ok(response);
    }
}
