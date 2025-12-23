package com.yumyumcoach.domain.diet.service;

import com.yumyumcoach.domain.diet.dto.CreateFoodRequest;
import com.yumyumcoach.domain.diet.dto.FoodDto;
import com.yumyumcoach.domain.diet.dto.UpdateFoodRequest;
import com.yumyumcoach.domain.diet.mapper.FoodMapper;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FoodService {

    private final FoodMapper foodMapper;

    public FoodService(FoodMapper foodMapper) {
        this.foodMapper = foodMapper;
    }

    @Transactional
    public void createFood(CreateFoodRequest request) {
        foodMapper.insertFood(request);
    }

    @Transactional
    public void updateFood(Long id, UpdateFoodRequest request) {
        int updated = foodMapper.updateFood(id, request);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found");
        }
    }

    @Transactional
    public void deleteFood(Long id) {
        int deleted = foodMapper.deleteFood(id);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found");
        }
    }

    @Transactional(readOnly = true)
    public FoodDto getFoodDetail(Long id) {
        FoodDto food = foodMapper.selectFoodById(id);
        if (food == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found");
        }
        return food;
    }

    @Transactional(readOnly = true)
    public List<FoodDto> getFoods(String keyword, int page, int size) {
        int offset = Math.max(page, 0) * Math.max(size, 1);
        int limit = Math.max(size, 1);
        return foodMapper.selectFoods(keyword, offset, limit);
    }
}

