package com.yumyumcoach.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 전체 게시글 목록 조회(피드) 응답 DTO.
 * - 페이징 정보(page, size, totalCount)
 * - 현재 페이지에 포함된 게시글 목록(posts)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPostsResponse {
    /**
     * 현재 페이지 번호 (1부터 시작)
     */
    private int page;

    /**
     * 페이지 사이즈
     */
    private int size;

    /**
     * 전체 게시글 개수
     */
    private long totalCount;

    /**
     * 현재 페이지에 포함된 게시글 목록
     */
    private List<PostResponse> posts;
}
