package com.yumyumcoach.domain.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDailyStat {
    private LocalDate date;
    private String dayOfWeekKr;
    private double durationMinutes;
    private double calories;
}
