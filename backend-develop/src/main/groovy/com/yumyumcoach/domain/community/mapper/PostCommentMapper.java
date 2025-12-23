package com.yumyumcoach.domain.community.mapper;

import com.yumyumcoach.domain.community.entity.PostComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostCommentMapper {

    List<PostComment> findByPostId(@Param("postId") Long postId);

    long countByPostId(@Param("postId") Long postId);

    PostComment findById(@Param("commentId") Long commentId);

    void insert(PostComment comment);

    void update(PostComment comment);

    void delete(@Param("commentId") Long commentId);

    void deleteByPostId(@Param("postId") Long postId);

    PostComment findByIdAndPostId(@Param("commentId") Long commentId, @Param("postId") Long postId);

    /**
     * 회원 탈퇴 시, 댓글 작성자 이메일을 시스템 탈퇴 계정으로 치환
     */
    int replaceAuthorEmail(@Param("fromEmail") String fromEmail,
                           @Param("toEmail") String toEmail);
}
