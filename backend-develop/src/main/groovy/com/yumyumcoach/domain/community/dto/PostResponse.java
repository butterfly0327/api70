package com.yumyumcoach.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글 응답 공통 DTO.
 * - 목록 / 상세 / 작성 / 수정 에서 모두 사용
 * - 댓글 목록은 별도 API (/api/posts/{postId}/comments) 로 제공
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    /**
     * 게시글 ID
     */
    private Long postId;

    /**
     * 작성자 ID
     * - User 도메인 연동 후 채워질 값
     */
    private Long authorId;

    /**
     * 작성자 닉네임
     */
    private String authorUsername;

    /**
     * 작성자 프로필 이미지 URL (CloudFront URL)
     */
    private String authorProfileImageUrl;

    /**
     * 게시글 제목
     */
    private String title;

    /**
     * 게시글 전체 본문
     * - 목록에서는 프리뷰 용도로 잘라서 사용할 수 있음
     */
    private String content;

    /**
     * 게시글 이미지 URL 목록 (CloudFront URL)
     * - post_images 테이블 기반 (order_index 오름차순 정렬 권장)
     * - 목록에서는 첫 번째 이미지만 썸네일로 사용할 수 있음
     */
    private List<String> images;

    /**
     * 좋아요 개수
     */
    private int likeCount;

    /**
     * 댓글 개수
     */
    private int commentCount;

    /**
     * 현재 사용자가 이 게시글에 좋아요를 눌렀는지 여부
     */
    private boolean isLikedByMe;

    /**
     * 게시글 작성 시각
     */
    private LocalDateTime createdAt;

    /**
     * 마지막 수정 시각
     */
    private LocalDateTime updatedAt;
}

