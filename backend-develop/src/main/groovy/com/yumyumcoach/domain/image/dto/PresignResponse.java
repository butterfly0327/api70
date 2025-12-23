package com.yumyumcoach.domain.image.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PresignResponse {
    private String presignedUrl; // 업로드용 URL (만료됨, 저장 X)
    private String objectKey;    // S3 저장 경로
    private String expiresAt;    // 만료 시각(문자열)
    private String cdnUrl;       // (옵션) CloudFront 붙이면 보여주기용 URL
}

