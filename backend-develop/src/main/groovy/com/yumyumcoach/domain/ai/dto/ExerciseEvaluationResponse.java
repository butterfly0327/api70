package com.yumyumcoach.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseEvaluationResponse {
    private boolean evaluated;
    private Long reviewId;
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private LocalDate evaluatedUntilDate;
    private String volumeStatus;
    private String recommendation;
    private String summaryText;
    private LocalDateTime generatedAt;
    private String rawText;
}
