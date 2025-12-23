package com.yumyumcoach.domain.exercise.mapper;

import com.yumyumcoach.domain.exercise.dto.ExerciseResponse;
import com.yumyumcoach.domain.exercise.dto.SearchDto;
import com.yumyumcoach.domain.exercise.dto.SearchResponse;
import com.yumyumcoach.domain.exercise.entity.Exercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExerciseMapper {
    List<Exercise> findAll();

    Exercise findById(@Param("exerciseId") Long exerciseId);

    List<SearchDto> searchExercise(@Param("keyword") String keyword,
                                   @Param("size") int size,
                                   @Param("offset") int offset);

    int countExercises(@Param("keyword") String keyword);
}
