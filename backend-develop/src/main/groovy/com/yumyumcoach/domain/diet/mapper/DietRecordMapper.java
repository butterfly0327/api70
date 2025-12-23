package com.yumyumcoach.domain.diet.mapper;

import com.yumyumcoach.domain.diet.dto.CreateDietRecordRequest;
import com.yumyumcoach.domain.diet.dto.DietRecordDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DietRecordMapper {

    int insertDietRecord(
            @Param("email") String email,
            @Param("req") CreateDietRecordRequest req
    );

    int updateDietRecord(
            @Param("id") Long id,
            @Param("email") String email,
            @Param("req") CreateDietRecordRequest req
    );

    int deleteDietRecord(
            @Param("id") Long id,
            @Param("email") String email
    );

    DietRecordDto selectDietRecordDetail(
            @Param("id") Long id,
            @Param("email") String email
    );

    List<DietRecordDto> selectDietRecordsByUser(
            @Param("email") String email,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    List<DietRecordDto> selectDietRecordsByUserAndDate(
            @Param("email") String email,
            @Param("recordedAt") LocalDate recordedAt,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    String selectOwnerEmail(@Param("id") Long id);

    LocalDateTime selectRecordedAtByIdAndEmail(
            @Param("id") Long id,
            @Param("email") String email
    );

}

