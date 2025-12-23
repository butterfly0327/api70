package com.yumyumcoach.domain.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yumyumcoach.domain.ai.dto.*;
import com.yumyumcoach.domain.ai.entity.AiExerciseReview;
import com.yumyumcoach.domain.ai.entity.AiMealPlan;
import com.yumyumcoach.domain.ai.entity.AiNutritionReview;
import com.yumyumcoach.domain.ai.mapper.AiExerciseReviewMapper;
import com.yumyumcoach.domain.ai.mapper.AiMealPlanMapper;
import com.yumyumcoach.domain.ai.mapper.AiNutritionReviewMapper;
import com.yumyumcoach.domain.stats.dto.DietDailyStat;
import com.yumyumcoach.domain.stats.dto.ExerciseDailyStat;
import com.yumyumcoach.domain.stats.dto.WeeklyStatsResponse;
import com.yumyumcoach.domain.stats.service.WeeklyStatsService;
import com.yumyumcoach.domain.user.dto.MyPageResponse;
import com.yumyumcoach.domain.user.service.UserService;
import com.yumyumcoach.global.exception.BusinessException;
import com.yumyumcoach.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiRecommendationService {
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final GeminiClient geminiClient;
    private final WeeklyStatsService weeklyStatsService;
    private final UserService userService;
    private final AiMealPlanMapper aiMealPlanMapper;
    private final AiNutritionReviewMapper aiNutritionReviewMapper;
    private final AiExerciseReviewMapper aiExerciseReviewMapper;

    @Transactional
    public MealPlanResponse generateMealPlan(String email, LocalDate targetDate) {
        LocalDate date = targetDate == null ? LocalDate.now(KST) : targetDate;
        WeeklyStatsResponse stats = weeklyStatsService.getWeeklyStats(email, date);
        MyPageResponse.Health health = userService.getMyPage(email).getHealth();

        String prompt = buildMealPrompt(health, stats, date);
        String aiText = geminiClient.generateContent(prompt);

        AiMealPlan plan = parseMealPlan(aiText);
        plan.setEmail(email);
        plan.setTargetDate(date);
        plan.setWeekdayKr(dayName(date));
        plan.setPromptContext(prompt);
        plan.setRawResponse(aiText);

        aiMealPlanMapper.upsertMealPlan(plan);
        AiMealPlan saved = aiMealPlanMapper.findByEmailAndDate(email, date);
        return toMealPlanResponse(saved, aiText, true);
    }

    @Transactional(readOnly = true)
    public MealPlanResponse getMealPlan(String email, LocalDate targetDate) {
        LocalDate date = targetDate == null ? LocalDate.now(KST) : targetDate;
        AiMealPlan plan = aiMealPlanMapper.findByEmailAndDate(email, date);
        if (plan == null) {
            return MealPlanResponse.builder()
                    .generated(false)
                    .targetDate(date)
                    .dayOfWeekKr(dayName(date))
                    .breakfast(new MealPlanItem())
                    .lunch(new MealPlanItem())
                    .dinner(new MealPlanItem())
                    .totalCalories(0.0)
                    .rawText(null)
                    .build();
        }
        return toMealPlanResponse(plan, plan.getRawResponse(), true);
    }

    @Transactional
    public NutritionEvaluationResponse generateNutritionReview(String email, LocalDate anchorDate) {
        LocalDate date = anchorDate == null ? LocalDate.now(KST) : anchorDate;
        WeeklyStatsResponse stats = weeklyStatsService.getWeeklyStats(email, date);
        MyPageResponse.Health health = userService.getMyPage(email).getHealth();

        List<DietDailyStat> usedDiet = stats.getDietStats().stream()
                .filter(d -> !d.getDate().isAfter(date))
                .toList();

        String prompt = buildNutritionPrompt(health, usedDiet, date);
        String rawText = geminiClient.generateContent(prompt);
        AiNutritionReview review = parseNutritionReview(rawText);
        review.setEmail(email);
        review.setWeekStartDate(stats.getWeekStartDate());
        review.setWeekEndDate(stats.getWeekEndDate());
        review.setEvaluatedUntilDate(date);
        review.setPromptContext(prompt);
        review.setRawResponse(rawText);

        aiNutritionReviewMapper.upsertReview(review);
        AiNutritionReview saved = aiNutritionReviewMapper.findByEmailAndWeek(email, stats.getWeekStartDate());
        return toNutritionResponse(saved, rawText, true);
    }

    @Transactional(readOnly = true)
    public NutritionEvaluationResponse getNutritionReview(String email, LocalDate anchorDate) {
        LocalDate date = anchorDate == null ? LocalDate.now(KST) : anchorDate;
        LocalDate weekStart = date.with(DayOfWeek.MONDAY);
        AiNutritionReview review = aiNutritionReviewMapper.findByEmailAndWeek(email, weekStart);
        if (review == null) {
            return NutritionEvaluationResponse.builder()
                    .evaluated(false)
                    .weekStartDate(weekStart)
                    .weekEndDate(weekStart.plusDays(6))
                    .evaluatedUntilDate(date)
                    .summaryText("아직 생성된 영양 평가가 없습니다.")
                    .build();
        }
        return toNutritionResponse(review, review.getRawResponse(), true);
    }

    @Transactional
    public ExerciseEvaluationResponse generateExerciseReview(String email, LocalDate anchorDate) {
        LocalDate date = anchorDate == null ? LocalDate.now(KST) : anchorDate;
        WeeklyStatsResponse stats = weeklyStatsService.getWeeklyStats(email, date);
        MyPageResponse.Health health = userService.getMyPage(email).getHealth();

        List<ExerciseDailyStat> usedExercises = stats.getExerciseStats().stream()
                .filter(ex -> !ex.getDate().isAfter(date))
                .toList();

        String prompt = buildExercisePrompt(health, usedExercises, date);
        String rawText = geminiClient.generateContent(prompt);
        AiExerciseReview review = parseExerciseReview(rawText);
        review.setEmail(email);
        review.setWeekStartDate(stats.getWeekStartDate());
        review.setWeekEndDate(stats.getWeekEndDate());
        review.setEvaluatedUntilDate(date);
        review.setPromptContext(prompt);
        review.setRawResponse(rawText);

        aiExerciseReviewMapper.upsertReview(review);
        AiExerciseReview saved = aiExerciseReviewMapper.findByEmailAndWeek(email, stats.getWeekStartDate());
        return toExerciseResponse(saved, rawText, true);
    }

    @Transactional(readOnly = true)
    public ExerciseEvaluationResponse getExerciseReview(String email, LocalDate anchorDate) {
        LocalDate date = anchorDate == null ? LocalDate.now(KST) : anchorDate;
        LocalDate weekStart = date.with(DayOfWeek.MONDAY);
        AiExerciseReview review = aiExerciseReviewMapper.findByEmailAndWeek(email, weekStart);
        if (review == null) {
            return ExerciseEvaluationResponse.builder()
                    .evaluated(false)
                    .weekStartDate(weekStart)
                    .weekEndDate(weekStart.plusDays(6))
                    .evaluatedUntilDate(date)
                    .summaryText("아직 생성된 운동 평가가 없습니다.")
                    .build();
        }
        return toExerciseResponse(review, review.getRawResponse(), true);
    }

    private MealPlanResponse toMealPlanResponse(AiMealPlan plan, String rawText, boolean generated) {
        return MealPlanResponse.builder()
                .generated(generated)
                .planId(plan.getId())
                .targetDate(plan.getTargetDate())
                .dayOfWeekKr(plan.getWeekdayKr())
                .breakfast(MealPlanItem.builder()
                        .menu(plan.getBreakfastMenu())
                        .calories(plan.getBreakfastCalories())
                        .comment(plan.getBreakfastComment())
                        .build())
                .lunch(MealPlanItem.builder()
                        .menu(plan.getLunchMenu())
                        .calories(plan.getLunchCalories())
                        .comment(plan.getLunchComment())
                        .build())
                .dinner(MealPlanItem.builder()
                        .menu(plan.getDinnerMenu())
                        .calories(plan.getDinnerCalories())
                        .comment(plan.getDinnerComment())
                        .build())
                .totalCalories(plan.getTotalCalories())
                .generatedAt(plan.getUpdatedAt() != null ? plan.getUpdatedAt() : plan.getCreatedAt())
                .rawText(rawText)
                .build();
    }

    private NutritionEvaluationResponse toNutritionResponse(AiNutritionReview review, String rawText, boolean evaluated) {
        return NutritionEvaluationResponse.builder()
                .evaluated(evaluated)
                .reviewId(review.getId())
                .weekStartDate(review.getWeekStartDate())
                .weekEndDate(review.getWeekEndDate())
                .evaluatedUntilDate(review.getEvaluatedUntilDate())
                .carbohydrateStatus(review.getCarbohydrateStatus())
                .proteinStatus(review.getProteinStatus())
                .fatStatus(review.getFatStatus())
                .calorieStatus(review.getCalorieStatus())
                .summaryText(review.getSummaryText())
                .generatedAt(review.getUpdatedAt() != null ? review.getUpdatedAt() : review.getCreatedAt())
                .rawText(rawText)
                .build();
    }

    private ExerciseEvaluationResponse toExerciseResponse(AiExerciseReview review, String rawText, boolean evaluated) {
        return ExerciseEvaluationResponse.builder()
                .evaluated(evaluated)
                .reviewId(review.getId())
                .weekStartDate(review.getWeekStartDate())
                .weekEndDate(review.getWeekEndDate())
                .evaluatedUntilDate(review.getEvaluatedUntilDate())
                .volumeStatus(review.getVolumeStatus())
                .recommendation(review.getRecommendation())
                .summaryText(review.getSummaryText())
                .generatedAt(review.getUpdatedAt() != null ? review.getUpdatedAt() : review.getCreatedAt())
                .rawText(rawText)
                .build();
    }

    private String buildMealPrompt(MyPageResponse.Health health, WeeklyStatsResponse stats, LocalDate targetDate) {
        List<DietDailyStat> weekSoFar = stats.getDietStats().stream()
                .filter(d -> !d.getDate().isAfter(targetDate))
                .sorted(Comparator.comparing(DietDailyStat::getDate))
                .toList();

        StringBuilder builder = new StringBuilder();
        builder.append("다음 정보를 바탕으로 한국어 JSON만 반환해 주세요.\n");
        builder.append("출력 포맷: {\"breakfast\":{\"menu\":...,\"calories\":숫자,\"comment\":...}, ");
        builder.append("\"lunch\":{...}, \"dinner\":{...}, \"totalCalories\":숫자}\n");
        builder.append("각 menu는 음식과 곁들이는 재료를 3~5개 bullet 없이 콤마로 나열하고, comment는 한 줄 평가입니다.\n");
        builder.append("목표 날짜: ").append(targetDate).append(" (요일: ").append(dayName(targetDate)).append(")\n");

        builder.append("사용자 건강 정보: ");
        builder.append(String.format(Locale.KOREAN,
                "키 %.1fcm, 현재체중 %.1fkg, 목표체중 %.1fkg, 활동수준 %s, 당뇨:%s, 고혈압:%s, 고지혈증:%s, 기타:%s, 목표:%s\n",
                nullSafe(health.getHeight(), 0.0),
                nullSafe(health.getWeight(), 0.0),
                nullSafe(health.getGoalWeight(), 0.0),
                textOrDefault(health.getActivityLevel(), "정보없음"),
                flag(health.getHasDiabetes()),
                flag(health.getHasHypertension()),
                flag(health.getHasHyperlipidemia()),
                textOrDefault(health.getOtherDisease(), "없음"),
                textOrDefault(health.getGoal(), "없음")
        ));

        builder.append("이번 주 월요일부터 현재까지 섭취 요약:\n");
        weekSoFar.forEach(d -> builder.append(String.format(Locale.KOREAN,
                "%s(%s): 탄수 %.1fg, 단백 %.1fg, 지방 %.1fg, 열량 %.1fkcal\n",
                d.getDate(), d.getDayOfWeekKr(), d.getCarbs(), d.getProtein(), d.getFat(), d.getCalories()
        )));

        builder.append("요청: 1) 아침/점심/저녁별 메뉴 제안 2) 각 끼니 칼로리 숫자만 3) 각 끼니 한 줄 comment 4) totalCalories 합산 숫자. 추가 설명이나 문장은 쓰지 말 것.");
        return builder.toString();
    }

    private String buildNutritionPrompt(MyPageResponse.Health health, List<DietDailyStat> dietStats, LocalDate anchorDate) {
        LocalTime now = LocalTime.now(KST);
        StringBuilder builder = new StringBuilder();
        builder.append("주어진 데이터로 영양 평가를 JSON으로만 작성하세요.\n");
        builder.append("포맷: {\"carbohydrateStatus\":\"LOW|ADEQUATE|HIGH\", ");
        builder.append("\"proteinStatus\":..., \"fatStatus\":..., \"calorieStatus\":..., \"summaryText\":\"문장\"}\n");
        builder.append("평가 기준: 주 전체는 7일, 현재 주는 ").append(anchorDate.getDayOfWeek().getValue()).append("일차까지 진행됨. ");
        builder.append("현재 시간 ").append(now).append(" 기준으로 아침(08:00), 점심(14:00), 저녁(20:00) 지나지 않은 끼니는 앞으로 먹을 수 있음을 반영.\n");

        builder.append("건강 정보: 키").append(nullSafe(health.getHeight(), 0.0)).append("cm, 몸무게")
                .append(nullSafe(health.getWeight(), 0.0)).append("kg, 목표")
                .append(nullSafe(health.getGoalWeight(), 0.0)).append("kg, 질환:")
                .append(flag(health.getHasDiabetes())).append('/')
                .append(flag(health.getHasHypertension())).append('/')
                .append(flag(health.getHasHyperlipidemia())).append("\n");

        builder.append("섭취 데이터:\n");
        dietStats.forEach(d -> builder.append(String.format(Locale.KOREAN,
                "%s %s: 탄수 %.1fg, 단백 %.1fg, 지방 %.1fg, 열량 %.1fkcal\n",
                d.getDate(), d.getDayOfWeekKr(), d.getCarbs(), d.getProtein(), d.getFat(), d.getCalories()
        )));

        builder.append("요청: 각 status는 LOW/ ADEQUATE/ HIGH 중 하나로, summaryText는 2~3문장으로 개선 방향을 포함. 여분 텍스트 금지.");
        return builder.toString();
    }

    private String buildExercisePrompt(MyPageResponse.Health health, List<ExerciseDailyStat> exerciseStats, LocalDate anchorDate) {
        StringBuilder builder = new StringBuilder();
        builder.append("주간 운동 데이터를 평가하고 JSON만 반환하세요.\n");
        builder.append("포맷: {\"volumeStatus\":\"LOW|ADEQUATE|HIGH\", ");
        builder.append("\"recommendation\":\"간단 추천\", \"summaryText\":\"문장\"}\n");
        builder.append("현재 주 진행일: ").append(anchorDate.getDayOfWeek().getValue()).append("일차 (월요일 시작).\n");
        builder.append("몸무게 목표: 현재 ").append(nullSafe(health.getWeight(), 0.0)).append("kg -> 목표 ")
                .append(nullSafe(health.getGoalWeight(), 0.0)).append("kg. 활동수준 ")
                .append(textOrDefault(health.getActivityLevel(), "정보없음")).append(".\n");

        builder.append("운동 기록:\n");
        exerciseStats.forEach(ex -> builder.append(String.format(Locale.KOREAN,
                "%s %s: 운동시간 %.1f분, 소모열량 %.1fkcal\n",
                ex.getDate(), ex.getDayOfWeekKr(), ex.getDurationMinutes(), ex.getCalories()
        )));

        builder.append("요청: volumeStatus는 LOW/ ADEQUATE/ HIGH 중 하나, recommendation은 1~2문장 운동 제안, summaryText는 2문장으로 평가. 여분 텍스트 금지.");
        return builder.toString();
    }

    private AiMealPlan parseMealPlan(String jsonText) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(jsonText);
            return AiMealPlan.builder()
                    .breakfastMenu(root.path("breakfast").path("menu").asText(""))
                    .breakfastCalories(root.path("breakfast").path("calories").asDouble(0))
                    .breakfastComment(root.path("breakfast").path("comment").asText(""))
                    .lunchMenu(root.path("lunch").path("menu").asText(""))
                    .lunchCalories(root.path("lunch").path("calories").asDouble(0))
                    .lunchComment(root.path("lunch").path("comment").asText(""))
                    .dinnerMenu(root.path("dinner").path("menu").asText(""))
                    .dinnerCalories(root.path("dinner").path("calories").asDouble(0))
                    .dinnerComment(root.path("dinner").path("comment").asText(""))
                    .totalCalories(root.path("totalCalories").asDouble(0))
                    .build();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Gemini 식단 응답 파싱에 실패했습니다.");
        }
    }

    private AiNutritionReview parseNutritionReview(String jsonText) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(jsonText);
            return AiNutritionReview.builder()
                    .carbohydrateStatus(root.path("carbohydrateStatus").asText("UNKNOWN"))
                    .proteinStatus(root.path("proteinStatus").asText("UNKNOWN"))
                    .fatStatus(root.path("fatStatus").asText("UNKNOWN"))
                    .calorieStatus(root.path("calorieStatus").asText("UNKNOWN"))
                    .summaryText(root.path("summaryText").asText(jsonText))
                    .build();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Gemini 영양 응답 파싱에 실패했습니다.");
        }
    }

    private AiExerciseReview parseExerciseReview(String jsonText) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(jsonText);
            return AiExerciseReview.builder()
                    .volumeStatus(root.path("volumeStatus").asText("UNKNOWN"))
                    .recommendation(root.path("recommendation").asText(""))
                    .summaryText(root.path("summaryText").asText(jsonText))
                    .build();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Gemini 운동 응답 파싱에 실패했습니다.");
        }
    }

    private AiNutritionReview emptyNutritionReview(String email, WeeklyStatsResponse stats, LocalDate anchorDate, String message) {
        return AiNutritionReview.builder()
                .email(email)
                .weekStartDate(stats.getWeekStartDate())
                .weekEndDate(stats.getWeekEndDate())
                .evaluatedUntilDate(anchorDate)
                .carbohydrateStatus("NO_DATA")
                .proteinStatus("NO_DATA")
                .fatStatus("NO_DATA")
                .calorieStatus("NO_DATA")
                .summaryText(message)
                .build();
    }

    private AiExerciseReview emptyExerciseReview(String email, WeeklyStatsResponse stats, LocalDate anchorDate, String message) {
        return AiExerciseReview.builder()
                .email(email)
                .weekStartDate(stats.getWeekStartDate())
                .weekEndDate(stats.getWeekEndDate())
                .evaluatedUntilDate(anchorDate)
                .volumeStatus("NO_DATA")
                .recommendation(message)
                .summaryText(message)
                .build();
    }

    private double nullSafe(Double value, double defaultValue) {
        return value == null ? defaultValue : value;
    }

    private String textOrDefault(String value, String defaultValue) {
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    private String flag(Boolean value) {
        if (value == null) {
            return "정보없음";
        }
        return Boolean.TRUE.equals(value) ? "있음" : "없음";
    }

    private String dayName(LocalDate date) {
        Map<DayOfWeek, String> names = Map.of(
                DayOfWeek.MONDAY, "월요일",
                DayOfWeek.TUESDAY, "화요일",
                DayOfWeek.WEDNESDAY, "수요일",
                DayOfWeek.THURSDAY, "목요일",
                DayOfWeek.FRIDAY, "금요일",
                DayOfWeek.SATURDAY, "토요일",
                DayOfWeek.SUNDAY, "일요일"
        );
        return names.getOrDefault(date.getDayOfWeek(), date.getDayOfWeek().name());
    }
}
