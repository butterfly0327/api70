package com.yumyumcoach.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== COMMON =====
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),

    // ===== AUTH =====
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않습니다."),
    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    AUTH_LOGIN_NOT_ALLOWED(HttpStatus.FORBIDDEN, "로그인할 수 없는 계정입니다."),
    AUTH_REFRESH_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "refresh token 이 없습니다."),
    AUTH_INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 refresh token 입니다."),
    AUTH_INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "올바른 이메일 형식이 아닙니다."),
    AUTH_INVALID_USERNAME_FORMAT(HttpStatus.BAD_REQUEST, "올바른 닉네임 형식이 아닙니다."),
    AUTH_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    AUTH_USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    AUTH_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "계정을 찾을 수 없습니다."),

    // ===== USER =====
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "프로필 정보를 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
    USER_TITLE_NOT_FOUND(HttpStatus.NOT_FOUND, "대표로 설정할 수 있는 타이틀을 찾을 수 없습니다."),
    FOLLOW_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 팔로우 요청입니다."),
    FOLLOW_TARGET_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우할 사용자를 찾을 수 없습니다."),
    FOLLOW_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 팔로우하고 있는 사용자입니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 관계를 찾을 수 없습니다."),

    // ===== COMMUNITY =====
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    POST_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 게시글에 대한 권한이 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 댓글에 대한 권한이 없습니다."),
    LIKE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 좋아요를 누른 게시글입니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글에 대해 눌러둔 좋아요가 없습니다."),

    // ===== CHALLENGE =====
    CHALLENGE_INVALID_MONTH_PARAM(HttpStatus.BAD_REQUEST,
            "조회 월 형식이 올바르지 않습니다."),
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 챌린지를 찾을 수 없습니다."),
    CHALLENGE_ALREADY_JOINED(HttpStatus.CONFLICT, "이미 참여 중인 챌린지입니다."),
    CHALLENGE_JOIN_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "현재는 해당 챌린지에 참여할 수 없습니다."),
    CHALLENGE_JOIN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 챌린지에 대한 참여 이력을 찾을 수 없습니다."),
    CHALLENGE_LEAVE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "현재는 해당 챌린지에서 나갈 수 없습니다."),
    CHALLENGE_ALREADY_LEFT(HttpStatus.CONFLICT, "이미 나간 챌린지입니다."),
    CHALLENGE_RECRUIT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "현재 모집 기간이 아닙니다."),
    CHALLENGE_ALREADY_COMPLETED(HttpStatus.CONFLICT, "이미 완료된 챌린지입니다."),
    CHALLENGE_RULE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "챌린지 룰이 설정되지 않았습니다."),
    CHALLENGE_RULE_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "챌린지 룰 설정이 올바르지 않습니다"),

    // ===== TITLE =====
    TITLE_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 뱃지에 대한 권한이 없습니다."),
    // ===== DIET =====
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 음식을 찾을 수 없습니다."),
    DIET_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 식단을 찾을 수 없습니다."),
    DIET_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 식단에 대한 권한이 없습니다."),

    // ===== EXERCISE =====
    EXERCISE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 운동을 찾을 수 없습니다."),
    EXERCISE_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 운동 기록을 찾을 수 없습니다."),
    EXERCISE_RECORD_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 운동 기록에 대한 권한이 없습니다."),
    EXERCISE_INVALID_KEYWORD(HttpStatus.BAD_REQUEST, "검색어는 2글자 이상 입력해주세요."),

    // ===== IMAGE =====
    IMAGE_UNSUPPORTED_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 이미지 형식입니다."),
    IMAGE_PRESIGN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Presigned URL 발급에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
