package com.yumyumcoach.domain.ai.chatbot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatJobStatusView {
    private Long jobId;
    private Long conversationId;
    private Long assistantMessageId;
    private String jobStatus;
    private String assistantStatus;
    private String assistantContent;
    private String assistantError;
    private String jobError;
}
