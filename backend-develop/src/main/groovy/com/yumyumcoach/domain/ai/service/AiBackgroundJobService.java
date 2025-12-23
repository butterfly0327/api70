package com.yumyumcoach.domain.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AiBackgroundJobService {

    private final AiRecommendationService aiRecommendationService;

    public void generateExerciseReviewAsync(String email, LocalDate anchorDate) {
        aiRecommendationService.generateExerciseReview(email, anchorDate);
    }

    public void generateNutritionReviewAsync(String email, LocalDate anchorDate) {
        aiRecommendationService.generateNutritionReview(email, anchorDate);
    }
}
