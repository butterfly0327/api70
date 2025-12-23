package com.yumyumcoach.domain.community.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostImage {
    private Long id;
    private Long postId;
    private String imageUrl;
    private int orderIndex;

    public static PostImage newImage(Long id, Long postId, String imageUrl, int orderIndex) {
        return PostImage.builder()
                .postId(postId)
                .imageUrl(imageUrl)
                .orderIndex(orderIndex)
                .build();
    }
}
