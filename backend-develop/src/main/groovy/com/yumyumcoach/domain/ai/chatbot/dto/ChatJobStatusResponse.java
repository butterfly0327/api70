package com.yumyumcoach.domain.ai.chatbot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatJobStatusResponse {
    private Long conversationId;
    private Long jobId;
    private Long assistantMessageId;
    private String status;
    private String assistantStatus;
    private String content;
    private String errorMessage;
}
