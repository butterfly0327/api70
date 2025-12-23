package com.yumyumcoach.domain.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 게시글 작성/수정 공통 요청 DTO.
 * - POST /api/posts
 * - PUT  /api/posts/{postId}
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    /**
     * 게시글 제목
     */
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이하여야 합니다.")
    private String title;

    /**
     * 게시글 본문
     */
    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 2000, message = "내용은 2000자 이하여야 합니다.")
    private String content;

    /**
     * 이미지 objectKey 목록
     */
    private List<String> images;
}

