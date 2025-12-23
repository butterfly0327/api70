package com.yumyumcoach.domain.diet.mapper;

import com.yumyumcoach.domain.diet.dto.CreateDietFoodRequest;
import com.yumyumcoach.domain.diet.dto.DietFoodDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DietFoodMapper {

    int insertDietFoods(
            @Param("dietId") Long dietId,
            @Param("items") List<CreateDietFoodRequest> items
    );

    int deleteDietFoodsByDietId(@Param("dietId") Long dietId);

    List<DietFoodDto> selectDietFoodsByDietId(@Param("dietId") Long dietId);
}

