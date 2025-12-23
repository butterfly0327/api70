package com.yumyumcoach.domain.community.controller;

import com.yumyumcoach.domain.community.dto.GetPostsRequest;
import com.yumyumcoach.domain.community.dto.GetPostsResponse;
import com.yumyumcoach.domain.community.dto.PostRequest;
import com.yumyumcoach.domain.community.dto.PostResponse;
import com.yumyumcoach.domain.community.service.PostService;
import com.yumyumcoach.global.common.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Community 게시글 관련 컨트롤러.
 * - /api/posts 하위 엔드포인트 담당
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    // 전체 게시글 목록 조회
    @GetMapping
    public GetPostsResponse getPosts(GetPostsRequest request) {
        String email = CurrentUser.email();
        return postService.getPosts(request, email);
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public PostResponse getPost(@PathVariable("postId") Long postId) {
        String email = CurrentUser.email();
        return postService.getPost(postId, email);
    }

    // 게시글 작성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse post(@Valid @RequestBody PostRequest request) {
        String email = CurrentUser.email();
        return postService.createPost(email, request);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public PostResponse updatePost(@PathVariable("postId") Long postId, @Valid @RequestBody PostRequest request) {
        String email = CurrentUser.email();
        return postService.updatePost(email, postId, request);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("postId") Long postId) {
        String email = CurrentUser.email();
        postService.deletePost(email, postId);
    }

    // 게시글 좋아요
    @PostMapping("/{postId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likePost(@PathVariable("postId") Long postId) {
        String email = CurrentUser.email();
        postService.likePost(email, postId);
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/{postId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikePost(@PathVariable("postId") Long postId) {
        String email = CurrentUser.email();
        postService.unlikePost(email, postId);
    }
}
