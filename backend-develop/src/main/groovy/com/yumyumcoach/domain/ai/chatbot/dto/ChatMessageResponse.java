package com.yumyumcoach.domain.ai.chatbot.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {
    private Long messageId;
    private String role;
    private String status;
    private String content;
    private String errorMessage;
    private LocalDateTime createdAt;
}
