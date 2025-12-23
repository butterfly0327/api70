package com.yumyumcoach.domain.ai.chatbot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatJobCreationResponse {
    private Long conversationId;
    private Long jobId;
    private Long assistantMessageId;
    private String status;
}
