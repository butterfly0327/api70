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
public class DietDailyStat {
    private LocalDate date;
    private String dayOfWeekKr;
    private double carbs;
    private double protein;
    private double fat;
    private double calories;
}
