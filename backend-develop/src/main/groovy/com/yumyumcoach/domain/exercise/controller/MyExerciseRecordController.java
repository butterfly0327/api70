package com.yumyumcoach.domain.exercise.controller;

import com.yumyumcoach.domain.exercise.dto.DeleteExerciseRecordResponse;
import com.yumyumcoach.domain.exercise.dto.ExerciseRecordRequest;
import com.yumyumcoach.domain.exercise.dto.ExerciseRecordResponse;
import com.yumyumcoach.domain.exercise.service.ExerciseService;
import com.yumyumcoach.global.common.CurrentUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/me/exercise-records")
public class MyExerciseRecordController {
    private final ExerciseService exerciseService;

    @GetMapping
    public List<ExerciseRecordResponse> getMyExerciseRecords(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate
    ) {
        String email = CurrentUser.email();
        return exerciseService.getMyExerciseRecords(email, recordDate);
    }

    @GetMapping("/{recordId}")
    public ExerciseRecordResponse getMyExerciseRecordDetail(
            @PathVariable("recordId") @Positive Long recordId
    ) {
        String email = CurrentUser.email();
        return exerciseService.getMyExerciseRecordDetail(email, recordId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<ExerciseRecordResponse> createMyExerciseRecord(
            @RequestBody @Valid @NotEmpty List<@Valid ExerciseRecordRequest> requests
    ) {
        String email = CurrentUser.email();
        return exerciseService.createMyExerciseRecords(email, requests);
    }

    @PutMapping("/{recordId}")
    public ExerciseRecordResponse updateMyExerciseRecord(
            @PathVariable("recordId") @Positive Long recordId,
            @RequestBody @Valid ExerciseRecordRequest request
    ) {
        String email = CurrentUser.email();
        return exerciseService.updateMyExerciseRecord(email, recordId, request);
    }

    @DeleteMapping("/{recordId}")
    public DeleteExerciseRecordResponse deleteMyExerciseRecord(
            @PathVariable("recordId") @Positive Long recordId
    ) {
        String email = CurrentUser.email();
        return exerciseService.deleteMyExerciseRecord(email, recordId);
    }
}