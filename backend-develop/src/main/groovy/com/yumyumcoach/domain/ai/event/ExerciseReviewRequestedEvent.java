package com.yumyumcoach.domain.ai.event;

import java.time.LocalDate;

public record ExerciseReviewRequestedEvent(String email, LocalDate anchorDate) {}

