package com.yumyumcoach.domain.ai.chatbot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatJobDetail {
    private Long jobId;
    private Long conversationId;
    private Long assistantMessageId;
    private String email;
    private String status;
    private String question;
}
