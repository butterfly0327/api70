package com.yumyumcoach.domain.ai.mapper;

import com.yumyumcoach.domain.ai.entity.AiExerciseReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface AiExerciseReviewMapper {
    AiExerciseReview findByEmailAndWeek(
            @Param("email") String email,
            @Param("weekStart") LocalDate weekStart
    );

    int upsertReview(@Param("review") AiExerciseReview review);
}
