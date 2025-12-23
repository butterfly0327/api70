package com.yumyumcoach.domain.exercise.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 운동 기록 생성/수정 요청 DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecordRequest {
    @NotNull
    private Long exerciseId;
    @NotNull
    private LocalDateTime recordedAt;
    @NotNull
    @Positive
    private Double durationMinutes;
}
