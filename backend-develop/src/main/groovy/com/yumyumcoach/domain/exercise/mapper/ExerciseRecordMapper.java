package com.yumyumcoach.domain.exercise.mapper;

import com.yumyumcoach.domain.exercise.entity.ExerciseRecord;
import com.yumyumcoach.domain.exercise.entity.ExerciseRecordWithExercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ExerciseRecordMapper {
    ExerciseRecordWithExercise findDetailByIdAndEmail(@Param("recordId") Long recordId,
                                                      @Param("email") String email);

    List<ExerciseRecordWithExercise> findByEmailAndDate(@Param("email") String email,
                                                        @Param("recordedAt") LocalDate recordedAt);

    void insert(ExerciseRecord exerciseRecord);

    void update(ExerciseRecord exerciseRecord);

    void delete(@Param("recordId") Long recordId, @Param("email") String email);

    // 추가: recordId로 소유자(email) 조회
    String findEmailByRecordId(@Param("recordId") Long recordId);
}
