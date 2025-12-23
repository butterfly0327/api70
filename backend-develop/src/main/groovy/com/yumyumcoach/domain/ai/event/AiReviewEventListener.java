package com.yumyumcoach.domain.ai.event;

import com.yumyumcoach.domain.ai.service.AiBackgroundJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class AiReviewEventListener {

    private final AiBackgroundJobService aiBackgroundJobService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onExerciseReviewRequested(ExerciseReviewRequestedEvent event) {
        aiBackgroundJobService.generateExerciseReviewAsync(event.email(), event.anchorDate());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onNutritionReviewRequested(NutritionReviewRequestedEvent event) {
        aiBackgroundJobService.generateNutritionReviewAsync(event.email(), event.anchorDate());
    }
}
