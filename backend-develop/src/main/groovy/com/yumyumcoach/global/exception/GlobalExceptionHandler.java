package com.yumyumcoach.global.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 의도된 비즈니스 예외
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
        ErrorCode ec = e.getErrorCode();
        return ResponseEntity.status(ec.getHttpStatus()).body(ErrorResponse.of(ec, e.getMessage()));
    }

    // JWT 만료
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException e) {
        return ResponseEntity.status(ErrorCode.AUTH_UNAUTHORIZED.getHttpStatus())
                .body(ErrorResponse.of(ErrorCode.AUTH_UNAUTHORIZED, "액세스 토큰이 만료되었습니다."));
    }

    // JWT 위조/형식오류/지원안함 등
    @ExceptionHandler({
            SignatureException.class,
            MalformedJwtException.class,
            UnsupportedJwtException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidJwt(Exception e) {
        return ResponseEntity.status(ErrorCode.AUTH_UNAUTHORIZED.getHttpStatus())
                .body(ErrorResponse.from(ErrorCode.AUTH_UNAUTHORIZED));
    }

    // @Valid 실패 -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValid(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorCode.INVALID_REQUEST));
    }

    // 메서드 파라미터 검증 실패(@Validated, @Positive 등) -> 400
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.from(ErrorCode.INVALID_REQUEST));
    }

    // JSON 바디가 깨짐/파싱 실패 -> 400
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(Exception e) {
        return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorCode.INVALID_REQUEST));
    }

    // path/query 타입 미스매치 -> 400
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(Exception e) {
        return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorCode.INVALID_REQUEST));
    }

    // 나머지 -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}

