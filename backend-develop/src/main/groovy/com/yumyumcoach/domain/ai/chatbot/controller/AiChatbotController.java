package com.yumyumcoach.domain.ai.chatbot.controller;

import com.yumyumcoach.domain.ai.chatbot.dto.ChatConversationResponse;
import com.yumyumcoach.domain.ai.chatbot.dto.ChatGreetingResponse;
import com.yumyumcoach.domain.ai.chatbot.dto.ChatJobCreationResponse;
import com.yumyumcoach.domain.ai.chatbot.dto.ChatJobStatusResponse;
import com.yumyumcoach.domain.ai.chatbot.dto.ChatQuestionRequest;
import com.yumyumcoach.domain.ai.chatbot.service.AiChatbotService;
import com.yumyumcoach.global.common.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/chatbot")
public class AiChatbotController {

    private final AiChatbotService aiChatbotService;

    @PostMapping("/conversations/greetings")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatGreetingResponse createGreetingConversation() {
        String email = CurrentUser.email();
        return aiChatbotService.createGreetingConversation(email);
    }

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatJobCreationResponse createChatJob(@RequestBody ChatQuestionRequest request) {
        String email = CurrentUser.email();
        return aiChatbotService.createChatJob(email, request);
    }

    @GetMapping("/jobs/{jobId}")
    public ChatJobStatusResponse getJobStatus(@PathVariable("jobId") Long jobId) {
        String email = CurrentUser.email();
        return aiChatbotService.getJobStatus(email, jobId);
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ChatConversationResponse getConversationMessages(@PathVariable("conversationId") Long conversationId) {
        String email = CurrentUser.email();
        return aiChatbotService.getConversation(email, conversationId);
    }
}
