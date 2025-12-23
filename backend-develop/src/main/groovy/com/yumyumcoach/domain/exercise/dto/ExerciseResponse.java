package com.yumyumcoach.domain.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 운동 기본 정보 응답 DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseResponse {
    private Long exerciseId;
    private String name;
    private Double met;
    private String intensityLevel;
    private String type;
    private String description;
}
