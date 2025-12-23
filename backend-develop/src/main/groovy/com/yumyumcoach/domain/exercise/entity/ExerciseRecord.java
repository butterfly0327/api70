package com.yumyumcoach.domain.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 운동 기록 엔티티.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecord {
    private Long id;
    private String email;
    private Long exerciseId;
    private LocalDateTime recordedAt;
    private Double durationMinutes;
    private Double calories;
}
