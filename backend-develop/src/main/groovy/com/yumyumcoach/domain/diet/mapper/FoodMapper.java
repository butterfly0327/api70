package com.yumyumcoach.domain.diet.mapper;

import com.yumyumcoach.domain.diet.dto.CreateFoodRequest;
import com.yumyumcoach.domain.diet.dto.FoodDto;
import com.yumyumcoach.domain.diet.dto.UpdateFoodRequest;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FoodMapper {

    int insertFood(CreateFoodRequest request);

    int updateFood(@Param("id") Long id, @Param("request") UpdateFoodRequest request);

    int deleteFood(@Param("id") Long id);

    FoodDto selectFoodById(@Param("id") Long id);

    List<FoodDto> selectFoods(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
}

