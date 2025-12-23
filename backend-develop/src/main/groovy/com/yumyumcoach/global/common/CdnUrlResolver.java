package com.yumyumcoach.global.common;

import com.yumyumcoach.global.config.AwsS3Properties;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CdnUrlResolver {

    private final AwsS3Properties props;

    /**
     * DB에 저장된 값이 objectKey면 -> cdnBaseUrl + "/" + objectKey 로 변환
     */
    public String resolve(String objectKey) {
        if (objectKey == null || objectKey.isBlank()) return null;

        String base = props.getCdnBaseUrl();
        if (base == null || base.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        return base.endsWith("/") ? base + objectKey : base + "/" + objectKey;
    }
}
