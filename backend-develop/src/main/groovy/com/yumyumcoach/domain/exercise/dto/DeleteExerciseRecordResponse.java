package com.yumyumcoach.domain.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 운동 기록 삭제 응답 DTO.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteExerciseRecordResponse {
    private Long recordId;
    private boolean deleted;
    private LocalDateTime deletedAt;
}
