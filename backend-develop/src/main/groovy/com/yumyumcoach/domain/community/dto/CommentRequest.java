package com.yumyumcoach.domain.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 작성/수정 요청 공통 DTO.
 * - POST /api/posts/{postId}/comments
 * - PUT  /api/posts/{postId}/comments/{commentId}
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    /**
     * 댓글 내용
     */
    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(max = 500, message = "댓글은 500자 이하여야 합니다.")
    private String content;
}
