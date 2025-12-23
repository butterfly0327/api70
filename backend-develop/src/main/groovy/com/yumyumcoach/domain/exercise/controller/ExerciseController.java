package com.yumyumcoach.domain.exercise.controller;

import com.yumyumcoach.domain.exercise.dto.ExerciseResponse;
import com.yumyumcoach.domain.exercise.dto.SearchResponse;
import com.yumyumcoach.domain.exercise.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping
    public List<ExerciseResponse> getExercises() {
        return exerciseService.getExercises();
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(@RequestParam ("keyword") String keyword,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "size", defaultValue = "5") int size) {
        SearchResponse response = exerciseService.searchExercise(keyword, page, size);
        return ResponseEntity.ok(response);
    }
}
