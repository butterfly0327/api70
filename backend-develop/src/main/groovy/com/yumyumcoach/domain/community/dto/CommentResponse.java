package com.yumyumcoach.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 댓글 한개 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    /**
     * 댓글 ID
     */
    private Long commentId;

    /**
     * 게시글 ID
     */
    private Long postId;

    /**
     * 작성자 ID
     */
    private Long authorId;

    /**
     * 작성자 닉네임
     */
    private String authorUsername;

    /**
     * 작성자 프로필 이미지 URL
     */
    private String authorProfileImageUrl;

    /**
     * 댓글 내용
     */
    private String content;

    /**
     * 댓글 작성 시각
     */
    private LocalDateTime createdAt;
}
