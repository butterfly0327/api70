package com.yumyumcoach.domain.ai.event;

import java.time.LocalDate;

public record NutritionReviewRequestedEvent(String email, LocalDate anchorDate) {}
