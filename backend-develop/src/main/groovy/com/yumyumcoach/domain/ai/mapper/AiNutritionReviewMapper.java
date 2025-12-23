package com.yumyumcoach.domain.ai.mapper;

import com.yumyumcoach.domain.ai.entity.AiNutritionReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface AiNutritionReviewMapper {
    AiNutritionReview findByEmailAndWeek(
            @Param("email") String email,
            @Param("weekStart") LocalDate weekStart
    );

    int upsertReview(@Param("review") AiNutritionReview review);
}
