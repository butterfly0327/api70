package com.yumyumcoach.domain.ai.chatbot.service;

import com.yumyumcoach.domain.ai.chatbot.dto.*;
import com.yumyumcoach.domain.ai.chatbot.entity.AiChatConversation;
import com.yumyumcoach.domain.ai.chatbot.entity.AiChatJob;
import com.yumyumcoach.domain.ai.chatbot.entity.AiChatMessage;
import com.yumyumcoach.domain.ai.chatbot.entity.ChatJobStatus;
import com.yumyumcoach.domain.ai.chatbot.entity.ChatMessageRole;
import com.yumyumcoach.domain.ai.chatbot.entity.ChatMessageStatus;
import com.yumyumcoach.domain.ai.chatbot.event.ChatJobRequestedEvent;
import com.yumyumcoach.domain.ai.chatbot.mapper.AiChatConversationMapper;
import com.yumyumcoach.domain.ai.chatbot.mapper.AiChatJobMapper;
import com.yumyumcoach.domain.ai.chatbot.mapper.AiChatMessageMapper;
import com.yumyumcoach.domain.ai.service.GeminiClient;
import com.yumyumcoach.domain.stats.dto.DietDailyStat;
import com.yumyumcoach.domain.stats.dto.ExerciseDailyStat;
import com.yumyumcoach.domain.stats.dto.WeeklyStatsResponse;
import com.yumyumcoach.domain.stats.service.WeeklyStatsService;
import com.yumyumcoach.domain.user.dto.MyPageResponse;
import com.yumyumcoach.domain.user.service.UserService;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiChatbotService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final Map<DayOfWeek, String> DAY_OF_WEEK_KR = Map.of(
            DayOfWeek.MONDAY, "월요일",
            DayOfWeek.TUESDAY, "화요일",
            DayOfWeek.WEDNESDAY, "수요일",
            DayOfWeek.THURSDAY, "목요일",
            DayOfWeek.FRIDAY, "금요일",
            DayOfWeek.SATURDAY, "토요일",
            DayOfWeek.SUNDAY, "일요일"
    );
    private static final int ERROR_MESSAGE_LIMIT = 300;

    private final AiChatConversationMapper conversationMapper;
    private final AiChatMessageMapper messageMapper;
    private final AiChatJobMapper jobMapper;
    private final GeminiClient geminiClient;
    private final WeeklyStatsService weeklyStatsService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ChatGreetingResponse createGreetingConversation(String email) {
        AiChatConversation conversation = AiChatConversation.builder()
                .email(email)
                .build();
        conversationMapper.insertConversation(conversation);

        AiChatMessage assistantMessage = AiChatMessage.builder()
                .conversationId(conversation.getId())
                .role(ChatMessageRole.ASSISTANT.name())
                .status(ChatMessageStatus.COMPLETE.name())
                .content(buildGreetingMessage())
                .build();
        messageMapper.insertMessage(assistantMessage);

        AiChatMessage savedAssistant = messageMapper.findByIdAndEmail(assistantMessage.getId(), email);

        ChatMessageResponse greetingMessage = ChatMessageResponse.builder()
                .messageId(assistantMessage.getId())
                .role(ChatMessageRole.ASSISTANT.name())
                .status(ChatMessageStatus.COMPLETE.name())
                .content(savedAssistant != null ? savedAssistant.getContent() : assistantMessage.getContent())
                .errorMessage(null)
                .createdAt(savedAssistant != null ? savedAssistant.getCreatedAt() : null)
                .build();

        return ChatGreetingResponse.builder()
                .conversationId(conversation.getId())
                .assistantMessage(greetingMessage)
                .build();
    }

    @Transactional
    public ChatJobCreationResponse createChatJob(String email, ChatQuestionRequest request) {
        if (request == null || request.getQuestion() == null || request.getQuestion().isBlank()) {
            throw new BusinessException(ErrorCode.AI_CHAT_INVALID_QUESTION, "질문이 비어 있습니다.");
        }

        AiChatConversation conversation = resolveConversation(email, request.getConversationId());

        AiChatMessage userMessage = AiChatMessage.builder()
                .conversationId(conversation.getId())
                .role(ChatMessageRole.USER.name())
                .status(ChatMessageStatus.COMPLETE.name())
                .content(request.getQuestion().trim())
                .build();
        messageMapper.insertMessage(userMessage);

        AiChatMessage assistantMessage = AiChatMessage.builder()
                .conversationId(conversation.getId())
                .role(ChatMessageRole.ASSISTANT.name())
                .status(ChatMessageStatus.PENDING.name())
                .build();
        messageMapper.insertMessage(assistantMessage);

        AiChatJob job = AiChatJob.builder()
                .conversationId(conversation.getId())
                .userMessageId(userMessage.getId())
                .assistantMessageId(assistantMessage.getId())
                .status(ChatJobStatus.PENDING.name())
                .build();
        jobMapper.insertJob(job);

        eventPublisher.publishEvent(new ChatJobRequestedEvent(job.getId(), email));

        return ChatJobCreationResponse.builder()
                .conversationId(conversation.getId())
                .jobId(job.getId())
                .assistantMessageId(assistantMessage.getId())
                .status(ChatJobStatus.PENDING.name())
                .build();
    }

    @Transactional(readOnly = true)
    public ChatJobStatusResponse getJobStatus(String email, Long jobId) {
        ChatJobStatusView view = jobMapper.findStatusByIdAndEmail(jobId, email);
        if (view == null) {
            throw new BusinessException(ErrorCode.AI_CHAT_JOB_NOT_FOUND);
        }
        return ChatJobStatusResponse.builder()
                .conversationId(view.getConversationId())
                .jobId(view.getJobId())
                .assistantMessageId(view.getAssistantMessageId())
                .status(view.getJobStatus())
                .assistantStatus(view.getAssistantStatus())
                .content(view.getAssistantContent())
                .errorMessage(view.getAssistantError() != null ? view.getAssistantError() : view.getJobError())
                .build();
    }

    @Transactional(readOnly = true)
    public ChatConversationResponse getConversation(String email, Long conversationId) {
        AiChatConversation conversation = conversationMapper.findByIdAndEmail(conversationId, email);
        if (conversation == null) {
            throw new BusinessException(ErrorCode.AI_CHAT_CONVERSATION_NOT_FOUND);
        }
        List<ChatMessageResponse> messages = messageMapper.findByConversation(conversationId, email).stream()
                .map(message -> ChatMessageResponse.builder()
                        .messageId(message.getId())
                        .role(message.getRole())
                        .status(message.getStatus())
                        .content(message.getContent())
                        .errorMessage(message.getErrorMessage())
                        .createdAt(message.getCreatedAt())
                        .build())
                .toList();
        return ChatConversationResponse.builder()
                .conversationId(conversation.getId())
                .messages(messages)
                .build();
    }

    @Transactional
    public void processJob(Long jobId, String email) {
        ChatJobDetail detail = jobMapper.findDetailById(jobId);
        if (detail == null || !Objects.equals(detail.getEmail(), email)) {
            return;
        }
        if (!ChatJobStatus.PENDING.name().equals(detail.getStatus())) {
            return;
        }

        try {
            LocalDate today = LocalDate.now(KST);
            WeeklyStatsResponse stats = weeklyStatsService.getWeeklyStats(email, today);
            MyPageResponse.Health health = userService.getMyPage(email).getHealth();

            String prompt = buildPrompt(health, stats, today, detail.getQuestion());
            String answer = geminiClient.generateContent(prompt);

            messageMapper.updateAssistantMessage(detail.getAssistantMessageId(),
                    ChatMessageStatus.COMPLETE.name(), answer, null);
            jobMapper.updateJobStatus(jobId, ChatJobStatus.COMPLETED.name(), null);
        } catch (Exception e) {
            String errorMessage = trimErrorMessage(e.getMessage());
            messageMapper.updateAssistantMessage(detail.getAssistantMessageId(),
                    ChatMessageStatus.ERROR.name(), null, errorMessage);
            jobMapper.updateJobStatus(jobId, ChatJobStatus.FAILED.name(), errorMessage);
        }
    }

    private AiChatConversation resolveConversation(String email, Long conversationId) {
        if (conversationId == null) {
            AiChatConversation conversation = AiChatConversation.builder()
                    .email(email)
                    .build();
            conversationMapper.insertConversation(conversation);
            return conversation;
        }
        AiChatConversation existing = conversationMapper.findByIdAndEmail(conversationId, email);
        if (existing == null) {
            throw new BusinessException(ErrorCode.AI_CHAT_CONVERSATION_NOT_FOUND);
        }
        return existing;
    }

    private String buildGreetingMessage() {
        return "안녕하세요! 저는 식단/운동을 함께 관리하는 AI 코치 유미예요.\n" +
                "- 식단 추천, 운동 루틴, 기록 해석 등 무엇이든 편하게 물어보세요.\n" +
                "- 건강 정보나 목표가 바뀌면 설정에서 수정해 주시면 더 정확히 도와드릴게요.\n" +
                "- 응급 상황이나 진료가 필요한 경우에는 반드시 전문 의료진과 상담하세요.\n\n" +
                "지금 어떤 도움이 필요하신가요?";
    }

    private String buildPrompt(MyPageResponse.Health health, WeeklyStatsResponse stats, LocalDate today, String question) {
        StringBuilder sb = new StringBuilder();
        sb.append("당신은 사용자 맞춤형 건강/영양/운동 코치입니다. 제공된 데이터만 활용해 정확하고 근거 있는 답변을 주세요.\n");
        sb.append("[응답 원칙]\n");
        sb.append("- 반드시 한국어 존댓말을 사용하고, 따뜻하고 명확하게 안내합니다.\n");
        sb.append("- 불확실한 내용은 가정이나 전제를 명시하고 단정하지 않습니다.\n");
        sb.append("- 사용자가 바로 실천할 수 있는 조언을 구체적으로 제시합니다.\n\n");

        sb.append("[오늘 정보]\n");
        sb.append("- 날짜: ").append(today).append('\n');
        sb.append("- 요일: ").append(DAY_OF_WEEK_KR.getOrDefault(today.getDayOfWeek(), today.getDayOfWeek().name())).append("\n\n");

        sb.append("[사용자 건강 정보]\n");
        sb.append("- 키: ").append(formatNullableNumber(health != null ? health.getHeight() : null)).append(" cm\n");
        sb.append("- 현재 체중: ").append(formatNullableNumber(health != null ? health.getWeight() : null)).append(" kg\n");
        sb.append("- 목표 체중: ").append(formatNullableNumber(health != null ? health.getGoalWeight() : null)).append(" kg\n");
        sb.append("- 당뇨: ").append(formatBool(health != null ? health.getHasDiabetes() : null)).append('\n');
        sb.append("- 고혈압: ").append(formatBool(health != null ? health.getHasHypertension() : null)).append('\n');
        sb.append("- 고지혈증: ").append(formatBool(health != null ? health.getHasHyperlipidemia() : null)).append('\n');
        sb.append("- 기타 질환: ").append(defaultText(health != null ? health.getOtherDisease() : null)).append('\n');
        sb.append("- 활동 수준: ").append(defaultText(health != null ? health.getActivityLevel() : null)).append("\n\n");

        sb.append("[주간 식단 요약]\n");
        sb.append(formatDietStats(stats != null ? stats.getDietStats() : List.of()));
        sb.append("\n[주간 운동 요약]\n");
        sb.append(formatExerciseStats(stats != null ? stats.getExerciseStats() : List.of()));

        sb.append("\n[사용자 질문]\n");
        sb.append(question);

        return sb.toString();
    }

    private String formatDietStats(List<DietDailyStat> dietStats) {
        if (dietStats == null || dietStats.isEmpty()) {
            return "- 최근 주간 식단 기록이 없습니다.\n";
        }
        return dietStats.stream()
                .map(stat -> String.format("- %s (%s): 탄수화물 %.1fg, 단백질 %.1fg, 지방 %.1fg, 칼로리 %.1fkcal",
                        stat.getDate(),
                        stat.getDayOfWeekKr(),
                        stat.getCarbs(),
                        stat.getProtein(),
                        stat.getFat(),
                        stat.getCalories()))
                .collect(Collectors.joining("\n")) + "\n";
    }

    private String formatExerciseStats(List<ExerciseDailyStat> exerciseStats) {
        if (exerciseStats == null || exerciseStats.isEmpty()) {
            return "- 최근 주간 운동 기록이 없습니다.\n";
        }
        return exerciseStats.stream()
                .map(stat -> String.format("- %s (%s): 운동 %.0f분, 칼로리 %.1fkcal",
                        stat.getDate(),
                        stat.getDayOfWeekKr(),
                        stat.getDurationMinutes(),
                        stat.getCalories()))
                .collect(Collectors.joining("\n")) + "\n";
    }

    private String formatNullableNumber(Double value) {
        return value == null ? "미제공" : String.format("%.1f", value);
    }

    private String formatBool(Boolean value) {
        if (value == null) {
            return "미제공";
        }
        return Boolean.TRUE.equals(value) ? "예" : "아니오";
    }

    private String defaultText(String value) {
        if (value == null || value.isBlank()) {
            return "미제공";
        }
        return value;
    }

    private String trimErrorMessage(String message) {
        if (message == null) {
            return "알 수 없는 오류가 발생했습니다.";
        }
        if (message.length() <= ERROR_MESSAGE_LIMIT) {
            return message;
        }
        return message.substring(0, ERROR_MESSAGE_LIMIT);
    }
}
