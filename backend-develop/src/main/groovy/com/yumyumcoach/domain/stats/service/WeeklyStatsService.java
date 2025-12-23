package com.yumyumcoach.domain.stats.service;

import com.yumyumcoach.domain.stats.dto.DietDailyStat;
import com.yumyumcoach.domain.stats.dto.ExerciseDailyStat;
import com.yumyumcoach.domain.stats.dto.WeeklyStatsResponse;
import com.yumyumcoach.domain.stats.mapper.WeeklyStatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeeklyStatsService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private static final Map<DayOfWeek, String> DAY_OF_WEEK_KO = Map.of(
            DayOfWeek.MONDAY, "월요일",
            DayOfWeek.TUESDAY, "화요일",
            DayOfWeek.WEDNESDAY, "수요일",
            DayOfWeek.THURSDAY, "목요일",
            DayOfWeek.FRIDAY, "금요일",
            DayOfWeek.SATURDAY, "토요일",
            DayOfWeek.SUNDAY, "일요일"
    );

    private final WeeklyStatsMapper weeklyStatsMapper;

    public WeeklyStatsResponse getWeeklyStats(String email, LocalDate anchorDate) {
        LocalDate safeAnchor = anchorDate == null ? LocalDate.now(KST) : anchorDate;
        LocalDate weekStart = safeAnchor.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);

        Map<LocalDate, DietDailyStat> dietMap = toDietMap(email, weekStart, weekEnd);
        Map<LocalDate, ExerciseDailyStat> exerciseMap = toExerciseMap(email, weekStart, weekEnd);

        List<DietDailyStat> dietStats = new ArrayList<>();
        List<ExerciseDailyStat> exerciseStats = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = weekStart.plusDays(i);
            dietStats.add(dietMap.getOrDefault(date, emptyDietStat(date)));
            exerciseStats.add(exerciseMap.getOrDefault(date, emptyExerciseStat(date)));
        }

        return WeeklyStatsResponse.builder()
                .weekStartDate(weekStart)
                .weekEndDate(weekEnd)
                .dietStats(dietStats)
                .exerciseStats(exerciseStats)
                .build();
    }

    private Map<LocalDate, DietDailyStat> toDietMap(String email, LocalDate start, LocalDate end) {
        List<DietDailyStat> raw = weeklyStatsMapper.selectDietDailyTotals(email, start, end);
        Map<LocalDate, DietDailyStat> map = new HashMap<>();
        raw.forEach(item -> map.put(item.getDate(), attachDayOfWeek(item)));
        return map;
    }

    private Map<LocalDate, ExerciseDailyStat> toExerciseMap(String email, LocalDate start, LocalDate end) {
        List<ExerciseDailyStat> raw = weeklyStatsMapper.selectExerciseDailyTotals(email, start, end);
        Map<LocalDate, ExerciseDailyStat> map = new HashMap<>();
        raw.forEach(item -> map.put(item.getDate(), attachDayOfWeek(item)));
        return map;
    }

    private DietDailyStat attachDayOfWeek(DietDailyStat stat) {
        String day = DAY_OF_WEEK_KO.getOrDefault(stat.getDate().getDayOfWeek(), stat.getDate().getDayOfWeek().name());
        return DietDailyStat.builder()
                .date(stat.getDate())
                .dayOfWeekKr(day)
                .carbs(stat.getCarbs())
                .protein(stat.getProtein())
                .fat(stat.getFat())
                .calories(stat.getCalories())
                .build();
    }

    private ExerciseDailyStat attachDayOfWeek(ExerciseDailyStat stat) {
        String day = DAY_OF_WEEK_KO.getOrDefault(stat.getDate().getDayOfWeek(), stat.getDate().getDayOfWeek().name());
        return ExerciseDailyStat.builder()
                .date(stat.getDate())
                .dayOfWeekKr(day)
                .durationMinutes(stat.getDurationMinutes())
                .calories(stat.getCalories())
                .build();
    }

    private DietDailyStat emptyDietStat(LocalDate date) {
        return DietDailyStat.builder()
                .date(date)
                .dayOfWeekKr(DAY_OF_WEEK_KO.getOrDefault(date.getDayOfWeek(), date.getDayOfWeek().name()))
                .carbs(0)
                .protein(0)
                .fat(0)
                .calories(0)
                .build();
    }

    private ExerciseDailyStat emptyExerciseStat(LocalDate date) {
        return ExerciseDailyStat.builder()
                .date(date)
                .dayOfWeekKr(DAY_OF_WEEK_KO.getOrDefault(date.getDayOfWeek(), date.getDayOfWeek().name()))
                .durationMinutes(0)
                .calories(0)
                .build();
    }
}
