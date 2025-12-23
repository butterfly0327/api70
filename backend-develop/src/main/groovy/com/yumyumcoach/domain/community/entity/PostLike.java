package com.yumyumcoach.domain.community.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLike {

    /**
     * 좋아요 대상 게시글 ID
     */
    private Long postId;

    /**
     * 좋아요 누른 사용자 이메일
     * - 컬럼명은 email 이지만, 도메인에선 authorEmail로 통일
     */
    private String authorEmail;

    /**
     * 좋아요 누른 시각
     */
    private LocalDateTime createdAt;
}