package com.yumyumcoach.domain.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyStatsResponse {
    private LocalDate weekStartDate;
    private LocalDate weekEndDate;
    private List<DietDailyStat> dietStats;
    private List<ExerciseDailyStat> exerciseStats;
}
