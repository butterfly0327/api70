package com.yumyumcoach.domain.ai.chatbot.event;

import com.yumyumcoach.domain.ai.chatbot.service.AiChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class ChatJobEventListener {

    private final AiChatbotService aiChatbotService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onChatJobRequested(ChatJobRequestedEvent event) {
        aiChatbotService.processJob(event.jobId(), event.email());
    }
}
