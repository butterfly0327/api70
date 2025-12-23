package com.yumyumcoach.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 특정 게시글의 댓글 목록 조회 응답 DTO.
 * - GET /api/posts/{postId}/comments
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentsResponse {

    /**
     * 게시글 ID
     */
    private Long postId;

    /**
     * 해당 게시글에 달린 전체 댓글 개수
     */
    private long totalCount;

    /**
     * 댓글 목록
     */
    private List<CommentResponse> comments;
}
