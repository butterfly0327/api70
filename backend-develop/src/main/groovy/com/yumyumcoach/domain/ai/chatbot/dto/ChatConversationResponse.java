package com.yumyumcoach.domain.ai.chatbot.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatConversationResponse {
    private Long conversationId;
    private List<ChatMessageResponse> messages;
}
