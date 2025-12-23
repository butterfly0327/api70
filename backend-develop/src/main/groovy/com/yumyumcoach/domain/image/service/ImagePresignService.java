package com.yumyumcoach.domain.image.service;

import com.yumyumcoach.domain.auth.mapper.AccountMapper;
import com.yumyumcoach.domain.image.dto.ImagePurpose;
import com.yumyumcoach.domain.image.dto.PresignRequest;
import com.yumyumcoach.domain.image.dto.PresignResponse;
import com.yumyumcoach.global.config.AwsS3Properties;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImagePresignService {

    private static final Duration EXPIRE = Duration.ofMinutes(10);

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of("image/png", "image/jpeg", "image/webp");

    private final S3Presigner presigner;
    private final AwsS3Properties props;
    private final AccountMapper accountMapper;

    public PresignResponse createPresign(PresignRequest req, String email) {
        validate(req);

        String ext = extractExt(req.getFileName(), req.getContentType());
        String uuid = UUID.randomUUID().toString();
        String objectKey = buildObjectKey(req.getPurpose(), email, uuid, ext);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(props.getS3().getBucket())
                    .key(objectKey)
                    .contentType(req.getContentType())
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(EXPIRE)
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presigned = presigner.presignPutObject(presignRequest);

            String cdnUrl = buildCdnUrl(objectKey);

            return PresignResponse.builder()
                    .presignedUrl(presigned.url().toString())
                    .objectKey(objectKey)
                    .expiresAt(Instant.now().plus(EXPIRE).toString())
                    .cdnUrl(cdnUrl)
                    .build();

        } catch (Exception e) {
            // AWS 설정/권한/리전/버킷 등 뭐든 실패하면 여기로 옴
            e.printStackTrace();
            throw new BusinessException(ErrorCode.IMAGE_PRESIGN_FAILED);
        }
    }

    private void validate(PresignRequest req) {
        if (req == null
            || req.getPurpose() == null
            || req.getFileName() == null
            || req.getFileName().isBlank()
        ) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        if (req.getContentType() == null || !ALLOWED_CONTENT_TYPES.contains(req.getContentType())) {
            throw new BusinessException(ErrorCode.IMAGE_UNSUPPORTED_CONTENT_TYPE);
        }
    }

    private String buildObjectKey(ImagePurpose purpose, String email, String uuid, String ext) {
        return switch (purpose) {
            case PROFILE -> {
                Long userId = accountMapper.findIdByEmail(email);
                if (userId == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
                yield "profiles/" + userId + "/" + uuid + "." + ext;
            }
            case POST -> "posts/temp/" + uuid + "." + ext;
            case DIET -> "diets/temp/" + uuid + "." + ext;
            case CHALLENGE -> "challenges/" + uuid + "." + ext;
        };
    }

    private String extractExt(String fileName, String contentType) {
        int idx = fileName.lastIndexOf('.');
        if (idx > -1 && idx < fileName.length() - 1) {
            return fileName.substring(idx + 1).toLowerCase();
        }
        return switch (contentType) {
            case "image/png" -> "png";
            case "image/jpeg" -> "jpg";
            case "image/webp" -> "webp";
            default -> "bin";
        };
    }

    private String buildCdnUrl(String objectKey) {
        String base = props.getCdnBaseUrl();
        if (base == null || base.isBlank()) return null;
        return base.endsWith("/") ? base + objectKey : base + "/" + objectKey;
    }
}

