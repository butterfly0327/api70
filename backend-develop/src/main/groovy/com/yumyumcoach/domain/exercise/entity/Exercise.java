package com.yumyumcoach.domain.exercise.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 운동 기본 정보 엔티티.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    private Long id;
    private String name;
    private Double met;
    private String intensityLevel;
    private String type;
    private String description;
}
