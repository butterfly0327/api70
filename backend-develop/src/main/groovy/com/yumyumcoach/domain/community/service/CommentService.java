package com.yumyumcoach.domain.community.service;


import com.yumyumcoach.domain.community.dto.CommentRequest;
import com.yumyumcoach.domain.community.dto.CommentResponse;
import com.yumyumcoach.domain.community.dto.GetCommentsResponse;
import com.yumyumcoach.domain.community.entity.Post;
import com.yumyumcoach.domain.community.entity.PostComment;
import com.yumyumcoach.domain.community.mapper.PostCommentMapper;
import com.yumyumcoach.domain.community.mapper.PostMapper;
import com.yumyumcoach.global.common.CdnUrlResolver;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Community 댓글 관련 서비스.
 * - 댓글 목록 조회
 * - 댓글 작성/수정/삭제
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final PostMapper postMapper;
    private final PostCommentMapper postCommentMapper;
    private final CdnUrlResolver cdnUrlResolver;

    /**
     * 특정 게시글의 댓글 목록 조회
     * - GET /api/posts/{postId}/comments
     */
    public GetCommentsResponse getComments(Long postId) {
        // 1) 게시글 존재 여부 확인
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND, "댓글을 조회할 게시글을 찾을 수 없습니다.");
        }

        // 2) 댓글 목록 조회
        List<PostComment> comments = postCommentMapper.findByPostId(postId);

        // 3) CommentResponse 리스트로 매핑
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> CommentResponse.builder()
                        .commentId(comment.getId())
                        .postId(comment.getPostId())
                        // User 도메인 연동 전 : 일단 author 관련은 null로 세팅
                        .authorId(comment.getAuthorId())
                        .authorUsername(comment.getAuthorUsername())
                        .authorProfileImageUrl(
                                comment.getAuthorProfileImageUrl() == null ? null : cdnUrlResolver.resolve(comment.getAuthorProfileImageUrl())
                        )
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build()
                )
                .toList();

        return GetCommentsResponse.builder()
                .postId(postId)
                .totalCount(commentResponses.size())
                .comments(commentResponses)
                .build();
    }

    /**
     * 댓글 작성
     * - POST /api/posts/{postId}/comments
     */
    @Transactional
    public CommentResponse createComment(String loginUserEmail, Long postId, CommentRequest request) {
        // 1) 게시글 존재 여부 확인
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND, "댓글을 작성할 게시글을 찾을 수 없습니다.");
        }

        // 2) PostComment 엔티티 생성
        LocalDateTime now = LocalDateTime.now();
        PostComment comment = PostComment.builder()
                .postId(postId)
                .authorEmail(loginUserEmail)
                .content(request.getContent())
                .createdAt(now)
                .build();

        // 3) DB 저장 (id 자동 증가)
        postCommentMapper.insert(comment); // useGeneratedKeys=true 로 인해 comment.id 세팅됨

        PostComment saved = postCommentMapper.findByIdAndPostId(comment.getId(), postId);

        return CommentResponse.builder()
                .commentId(saved.getId())
                .postId(postId)
                .authorId(saved.getAuthorId())
                .authorUsername(saved.getAuthorUsername())
                .authorProfileImageUrl(
                        saved.getAuthorProfileImageUrl() == null ? null : cdnUrlResolver.resolve(saved.getAuthorProfileImageUrl())
                )
                .content(saved.getContent())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    /**
     * 댓글 수정
     * - PUT /api/posts/{postId}/comments/{commentId}
     */
    @Transactional
    public CommentResponse updateComment(String loginUserEmail, Long postId, Long commentId, CommentRequest request) {
        // 1) 댓글 조회 (postId와 commentId가 일치하는 댓글을 한 번에 조회)
        PostComment existing = postCommentMapper.findByIdAndPostId(commentId, postId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND, "수정할 댓글을 찾을 수 없습니다.");
        }

        // 2) 403: 작성자만 수정 가능
        if (existing.getAuthorEmail() == null ||
                !existing.getAuthorEmail().equalsIgnoreCase(loginUserEmail)) {
            throw new BusinessException(ErrorCode.COMMENT_FORBIDDEN);
        }

        // 3) 내용 수정
        postCommentMapper.update(PostComment.builder()
                .id(commentId)
                .content(request.getContent())
                .build());

        PostComment updated = postCommentMapper.findByIdAndPostId(commentId, postId);

        return CommentResponse.builder()
                .commentId(commentId)
                .postId(postId)
                .authorId(updated.getAuthorId())
                .authorUsername(updated.getAuthorUsername())
                .authorProfileImageUrl(
                        updated.getAuthorProfileImageUrl() == null ? null : cdnUrlResolver.resolve(updated.getAuthorProfileImageUrl())
                )
                .content(updated.getContent())
                .createdAt(updated.getCreatedAt())
                .build();
    }

    /**
     * 댓글 삭제
     * - DELETE /api/posts/{postId}/comments/{commentId}
     */
    @Transactional
    public void deleteComment(String loginUserEmail, Long postId, Long commentId) {
        // 1) 댓글 조회
        PostComment existing = postCommentMapper.findByIdAndPostId(commentId, postId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND, "삭제할 댓글을 찾을 수 없습니다.");
        }

        // 2) 403: 작성자만 삭제 가능
        if (existing.getAuthorEmail() == null ||
                !existing.getAuthorEmail().equalsIgnoreCase(loginUserEmail)) {
            throw new BusinessException(ErrorCode.COMMENT_FORBIDDEN);
        }

        // 3) 해당 댓글 1개만 삭제
        postCommentMapper.delete(commentId);
    }
}
