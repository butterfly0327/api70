package com.yumyumcoach.domain.ai.controller;

import com.yumyumcoach.domain.ai.dto.*;
import com.yumyumcoach.domain.ai.service.AiRecommendationService;
import com.yumyumcoach.global.common.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    @PostMapping("/meal-plans/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public MealPlanResponse generateMealPlan(@RequestBody(required = false) MealPlanRequest request) {
        String email = CurrentUser.email();
        LocalDate targetDate = request != null ? request.getTargetDate() : null;
        return aiRecommendationService.generateMealPlan(email, targetDate);
    }

    @GetMapping("/meal-plans/dates/{targetDate}")
    public MealPlanResponse getMealPlan(
            @PathVariable("targetDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        String email = CurrentUser.email();
        return aiRecommendationService.getMealPlan(email, targetDate);
    }

    @PostMapping("/nutrition-evaluations/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public NutritionEvaluationResponse generateNutritionReview(
            @RequestBody(required = false) NutritionEvaluationRequest request
    ) {
        String email = CurrentUser.email();
        LocalDate anchorDate = request != null ? request.getAnchorDate() : null;
        return aiRecommendationService.generateNutritionReview(email, anchorDate);
    }

    @GetMapping("/nutrition-evaluations/weeks/{anchorDate}")
    public NutritionEvaluationResponse getNutritionReview(
            @PathVariable("anchorDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate anchorDate
    ) {
        String email = CurrentUser.email();
        return aiRecommendationService.getNutritionReview(email, anchorDate);
    }

    @PostMapping("/exercise-evaluations/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciseEvaluationResponse generateExerciseReview(
            @RequestBody(required = false) ExerciseEvaluationRequest request
    ) {
        String email = CurrentUser.email();
        LocalDate anchorDate = request != null ? request.getAnchorDate() : null;
        return aiRecommendationService.generateExerciseReview(email, anchorDate);
    }

    @GetMapping("/exercise-evaluations/weeks/{anchorDate}")
    public ExerciseEvaluationResponse getExerciseReview(
            @PathVariable("anchorDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate anchorDate
    ) {
        String email = CurrentUser.email();
        return aiRecommendationService.getExerciseReview(email, anchorDate);
    }
}
