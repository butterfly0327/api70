package com.yumyumcoach.domain.diet.controller;

import com.yumyumcoach.domain.diet.dto.CreateFoodRequest;
import com.yumyumcoach.domain.diet.dto.FoodDto;
import com.yumyumcoach.domain.diet.dto.UpdateFoodRequest;
import com.yumyumcoach.domain.diet.service.FoodService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public ResponseEntity<Void> createFood(@Valid @RequestBody CreateFoodRequest request) {
        foodService.createFood(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFood(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFoodRequest request
    ) {
        foodService.updateFood(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable("id") Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDto> getFoodDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(foodService.getFoodDetail(id));
    }

    @GetMapping
    public ResponseEntity<List<FoodDto>> getFoods(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(foodService.getFoods(keyword, page, size));
    }
}

