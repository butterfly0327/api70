package com.yumyumcoach.domain.community.mapper;

import com.yumyumcoach.domain.community.entity.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostLikeMapper {
    /**
     * 특정 유저가 특정 게시글에 좋아요 눌렀는지 여부
     */
    boolean existsByPostIdAndAuthorEmail(@Param("postId") Long postId,
                                         @Param("authorEmail") String authorEmail);

    /**
     * 좋아요 추가
     */
    void insert(PostLike like);

    /**
     * 좋아요 취소
     */
    void deleteByPostIdAndAuthorEmail(@Param("postId") Long postId,
                                      @Param("authorEmail") String authorEmail);

    /**
     * 특정 게시글의 좋아요 개수
     */
    long countByPostId(@Param("postId") Long postId);
}
