package com.yumyumcoach.domain.ai.chatbot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatGreetingResponse {
    private Long conversationId;
    private ChatMessageResponse assistantMessage;
}
