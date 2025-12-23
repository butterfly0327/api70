package com.yumyumcoach.domain.community.mapper;

import com.yumyumcoach.domain.community.entity.PostImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostImageMapper {
    List<PostImage> findByPostId(@Param("postId") Long postId);

    void insert(PostImage postImage);

    void deleteByPostId(@Param("postId") Long postId);
}
