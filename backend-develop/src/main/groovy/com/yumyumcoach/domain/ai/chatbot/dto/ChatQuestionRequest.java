package com.yumyumcoach.domain.ai.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatQuestionRequest {
    private String question;
    private Long conversationId;
}
