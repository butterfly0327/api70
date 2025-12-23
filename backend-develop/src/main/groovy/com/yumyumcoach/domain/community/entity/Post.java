package com.yumyumcoach.domain.community.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String content;
//    private String category;
    private LocalDateTime createdAt;
    private int likes;
    private Long authorId;
    private String authorEmail;
    private String authorUsername;
    private String authorProfileImageUrl;

    // 나중에 category 추가시 주석 풀기
//    public void update(String title, String content, String category) {
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
//        this.category = category;
    }

    public void increaseLikes() {
        this.likes++;
    }

    public void decreaseLikes() {
        this.likes--;
    }

    // 나중에 category 추가시 주석 풀기
//    public static Post newPost(String title, String content, String category, String authorEmail) {
    public static Post newPost(String title, String content, String authorEmail) {
        return Post.builder()
                .title(title)
                .content(content)
//                .category(category)
                .authorEmail(authorEmail)
                .createdAt(LocalDateTime.now())
                .likes(0)
                .build();
    }
}
