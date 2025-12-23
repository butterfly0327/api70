package com.yumyumcoach.domain.ai.mapper;

import com.yumyumcoach.domain.ai.entity.AiMealPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface AiMealPlanMapper {
    AiMealPlan findByEmailAndDate(@Param("email") String email, @Param("targetDate") LocalDate targetDate);

    int upsertMealPlan(@Param("plan") AiMealPlan plan);
}
