package com.yumyumcoach.domain.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 운동 기록 + 운동 기본 정보 조인 결과.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecordWithExercise {
    private Long id;
    private Long exerciseId;
    private String exerciseName;
    private Double met;
    private String intensityLevel;
    private String type;
    private LocalDateTime recordedAt;
    private Double durationMinutes;
    private Double calories;
}
