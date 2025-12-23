package com.yumyumcoach.domain.community.service;

import com.yumyumcoach.domain.community.dto.GetPostsRequest;
import com.yumyumcoach.domain.community.dto.GetPostsResponse;
import com.yumyumcoach.domain.community.dto.PostRequest;
import com.yumyumcoach.domain.community.dto.PostResponse;
import com.yumyumcoach.domain.community.entity.Post;
import com.yumyumcoach.domain.community.entity.PostImage;
import com.yumyumcoach.domain.community.entity.PostLike;
import com.yumyumcoach.domain.community.mapper.PostCommentMapper;
import com.yumyumcoach.domain.community.mapper.PostImageMapper;
import com.yumyumcoach.domain.community.mapper.PostLikeMapper;
import com.yumyumcoach.domain.community.mapper.PostMapper;
import com.yumyumcoach.global.common.CdnUrlResolver;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Community 게시글 관련 서비스.
 * - 목록 조회
 * - 상세 조회
 * - 작성/수정/삭제
 * - 좋아요/좋아요 취소
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostMapper postMapper;
    private final PostImageMapper postImageMapper;
    private final PostLikeMapper postLikeMapper;
    private final PostCommentMapper postCommentMapper;
    private final CdnUrlResolver cdnUrlResolver;

    /**
     * 전체 게시글 목록(피드) 조회
     * - GET /api/posts
     */
    public GetPostsResponse getPosts(GetPostsRequest request, String loginUserEmail) {
        // 1) request.getPage(), request.getSize()를 사용해 페이징 조회
        int page = request.getPage();
        int size = request.getSize();
        int offset = (page - 1) * size;

        String keyword = request.getKeyword();
        String sort = request.getSort();

        // 2) 게시글 목록 조회
        List<Post> posts = postMapper.findPosts(offset, size, keyword, sort);
        if (posts.isEmpty()) {
            return GetPostsResponse.builder()
                    .page(page)
                    .size(size)
                    .totalCount(0L)
                    .posts(Collections.emptyList())
                    .build();
        }

        // 3) Post -> PostResponse 매핑
        List<PostResponse> postResponses = posts.stream()
                .map(post -> {
                    Long postId = post.getId();
                    // 이미지 목록
                    List<PostImage> postImages = postImageMapper.findByPostId(postId);
                    List<String> imageUrls = postImages.stream()
                            .map(PostImage::getImageUrl)
                            .map(cdnUrlResolver::resolve)
                            .toList();
                    // 댓글 개수
                    long commentCount = postCommentMapper.countByPostId(postId);
                    // 좋아요 개수 (posts.likes)
                    int likeCount = post.getLikes();
                    // 내가 좋아요 눌렀는지 여부
                    boolean isLikedByMe = false;
                    if (loginUserEmail != null) {
                        isLikedByMe = postLikeMapper.existsByPostIdAndAuthorEmail(postId, loginUserEmail);
                    }

                    return PostResponse.builder()
                            .postId(postId)
                            .authorId(post.getAuthorId())
                            .authorUsername(post.getAuthorUsername())
                            .authorProfileImageUrl(
                                    post.getAuthorProfileImageUrl() == null ? null : cdnUrlResolver.resolve(post.getAuthorProfileImageUrl())
                            )
                            .title(post.getTitle())
                            .content(post.getContent())
                            .images(imageUrls)
                            .likeCount(likeCount)
                            .commentCount((int) commentCount)
                            .isLikedByMe(isLikedByMe)
                            .createdAt(post.getCreatedAt())
                            .updatedAt(null)
                            .build();
                }).toList();

        // 4) 전체 개수 조회
        long totalCount = postMapper.countPosts(keyword);
        return GetPostsResponse.builder()
                .page(page)
                .size(size)
                .totalCount(totalCount)
                .posts(postResponses)
                .build();
    }

    /**
     * 게시글 상세 조회
     * - GET /api/posts/{postId}
     */
    public PostResponse getPost(Long postId, String loginUserEmail) {
        // 1) 게시글 조회
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        // 2) 이미지 목록 조회
        List<PostImage> postImages = postImageMapper.findByPostId(postId);
        List<String> imageUrls = postImages.stream()
                .map(PostImage::getImageUrl)
                .map(cdnUrlResolver::resolve)
                .toList();

        // 3) 댓글 개수 조회
        long commentCount = postCommentMapper.countByPostId(postId);

        // 4) 좋아요 개수 (posts.likes)
        int likeCount = post.getLikes();

        // 5) 현재 유저가 좋아요 눌렀는지 여부
        boolean isLikedByMe = loginUserEmail != null
                && postLikeMapper.existsByPostIdAndAuthorEmail(postId, loginUserEmail);

        return PostResponse.builder()
                .postId(post.getId())
                .authorId(post.getAuthorId())
                .authorUsername(post.getAuthorUsername())
                .authorProfileImageUrl(
                        post.getAuthorProfileImageUrl() == null ? null : cdnUrlResolver.resolve(post.getAuthorProfileImageUrl())
                )
                .title(post.getTitle())
                .content(post.getContent())
                .images(imageUrls)
                .likeCount(likeCount)
                .commentCount((int) commentCount)
                .isLikedByMe(isLikedByMe)
                .createdAt(post.getCreatedAt())
                .updatedAt(null)
                .build();
    }

    /**
     * 새 게시글 작성
     * - POST /api/posts
     */
    @Transactional
    public PostResponse createPost(String loginUserEmail, PostRequest request) {
        // 1) Post 엔티티 생성
        Post post = Post.builder()
                .authorEmail(loginUserEmail)
                .title(request.getTitle())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .likes(0)
                .build();

        // 2) posts 테이블 INSERT (id 자동 생성)
        postMapper.insert(post); // useGeneratedKeys="true" 로 인해 post.id 세팅됨
        Long postId = post.getId();

        // 3) 이미지가 있다면 post_images에 INSERT
        List<String> images = request.getImages() != null ? request.getImages() : Collections.emptyList();
        int orderIndex = 1;
        for (String imageUrl : images) {
            PostImage postImage = PostImage.builder()
                    .postId(postId)
                    .imageUrl(imageUrl)
                    .orderIndex(orderIndex++)
                    .build();
            postImageMapper.insert(postImage);
        }

        return getPost(postId, loginUserEmail);
    }

    /**
     * 게시글 수정
     * - PUT /api/posts/{postId}
     */
    @Transactional
    public PostResponse updatePost(String loginUserEmail, Long postId, PostRequest request) {
        // 1) 기존 게시글 조회
        Post existing = postMapper.findById(postId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        // 403: 작성자만 수정 가능
        if (existing.getAuthorEmail() == null ||
                !existing.getAuthorEmail().equalsIgnoreCase(loginUserEmail)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }

        // 2) 제목/내용 수정
        Post post = Post.builder()
                .id(postId)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        postMapper.update(post);

        // 3) 이미지 -> 기존 이미지 삭제 후 새로 저장
        postImageMapper.deleteByPostId(postId);
        List<String> images = request.getImages() != null ? request.getImages() : Collections.emptyList();
        int orderIndex = 1;
        for (String imageUrl : images) {
            PostImage postImage = PostImage.builder()
                    .postId(postId)
                    .imageUrl(imageUrl)
                    .orderIndex(orderIndex++)
                    .build();
            postImageMapper.insert(postImage);
        }

        return getPost(postId, loginUserEmail);
    }

    /**
     * 게시글 삭제
     * - DELETE /api/posts/{postId}
     */
    @Transactional
    public void deletePost(String loginUserEmail, Long postId) {
        Post existing = postMapper.findById(postId);
        if (existing == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        // 403: 작성자만 삭제 가능
        if (existing.getAuthorEmail() == null ||
                !existing.getAuthorEmail().equalsIgnoreCase(loginUserEmail)) {
            throw new BusinessException(ErrorCode.POST_FORBIDDEN);
        }

        // 1) 연관 데이터 삭제
        postImageMapper.deleteByPostId(postId);
        postCommentMapper.deleteByPostId(postId);
        // TODO: post_likes 전체 삭제용 메서드(PostLikeMapper.deleteByPostId) 추가해서 호출하면 더 깔끔함

        // 2) 게시글 삭제
        postMapper.delete(postId);
    }

    /**
     * 게시글 좋아요 추가
     * - POST /api/posts/{postId}/like
     */
    @Transactional
    public void likePost(String loginUserEmail, Long postId) {
        // 1) 해당 게시글이 존재하는지 확인
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }

        // 2) 이미 좋아요 눌렀는지 확인
        boolean alreadyLiked = postLikeMapper.existsByPostIdAndAuthorEmail(postId, loginUserEmail);
        if (alreadyLiked) {
            throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS, "좋아요를 누를 게시글을 찾을 수 없습니다.");
        }

        // 3) 좋아요 INSERT
        postLikeMapper.insert(PostLike.builder()
                .postId(postId)
                .authorEmail(loginUserEmail)
                .createdAt(LocalDateTime.now())
                .build());

        // 3) posts.likes +1
        postMapper.increaseLikes(postId);
    }

    /**
     * 게시글 좋아요 취소
     * - DELETE /api/posts/{postId}/like
     */
    @Transactional
    public void unlikePost(String loginUserEmail, Long postId) {
        // 1) 해당 게시글이 존재하는지 확인
        Post post = postMapper.findById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND, "좋아요를 취소할 게시글을 찾을 수 없습니다.");
        }

        // 2) 좋아요가 존재하는지 확인
        boolean alreadyLiked = postLikeMapper.existsByPostIdAndAuthorEmail(postId, loginUserEmail);
        if (!alreadyLiked) {
            throw new BusinessException(ErrorCode.LIKE_NOT_FOUND);
        }

        // 3) post_likes 에서 삭제
        postLikeMapper.deleteByPostIdAndAuthorEmail(postId, loginUserEmail);

        // 4) posts.likes -1
        postMapper.decreaseLikes(postId);
    }
}
