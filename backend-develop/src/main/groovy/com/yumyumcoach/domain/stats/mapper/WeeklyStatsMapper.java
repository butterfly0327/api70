package com.yumyumcoach.domain.stats.mapper;

import com.yumyumcoach.domain.stats.dto.DietDailyStat;
import com.yumyumcoach.domain.stats.dto.ExerciseDailyStat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WeeklyStatsMapper {
    List<DietDailyStat> selectDietDailyTotals(
            @Param("email") String email,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<ExerciseDailyStat> selectExerciseDailyTotals(
            @Param("email") String email,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
