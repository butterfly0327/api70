package com.yumyumcoach.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 전체 게시글 목록 조회 요청 DTO.
 * - GET /api/posts
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPostsRequest {
    /**
     * 1부터 시작하는 페이지 번호 (기본값 1)
     */
    @Builder.Default
    private int page = 1;

    /**
     * 페이지 사이즈 (기본값 20)
     */
    @Builder.Default
    private int size = 20;

    /**
     * [TODO] 검색 키워드
     * - 현재는 사용하지 않음
     * - 추후 제목/내용 검색 기능 추가 시 활용 예정
     */
    private String keyword;

    /**
     * [TODO] 정렬 기준
     * - 예: "LATEST", "POPULAR"
     * - 현재는 사용하지 않음
     */
    private String sort;
}
