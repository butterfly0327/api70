package com.yumyumcoach.domain.auth.mapper;

import com.yumyumcoach.domain.auth.entity.RefreshToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/*
refresh_tokens 테이블 매핑용 MyBatis 인터페이스 (생성/조회/삭제)
 */

@Mapper
public interface RefreshTokenMapper {

    void upsert(@Param("email") String email,
                @Param("tokenHash") String tokenHash,
                @Param("expiresAt")LocalDateTime expiresAt);

    RefreshToken findByEmail(@Param("email") String email);

    RefreshToken findByEmailAndHash(@Param("email") String email,
                                    @Param("tokenHash") String tokenHash);

    int deleteByEmailAndHash(@Param("email") String email,
                             @Param("tokenHash") String tokenHash);

    int deleteByEmail(@Param("email") String email);

    int deleteExpired();
}
