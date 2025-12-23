package com.yumyumcoach.domain.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 운동 기록 응답 DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecordResponse {
    private Long recordId;
    private Long exerciseId;
    private String exerciseName;
    private Double met;
    private String intensityLevel;
    private String type;
    private LocalDateTime recordedAt;
    private Double durationMinutes;
    private Double calories;
}
