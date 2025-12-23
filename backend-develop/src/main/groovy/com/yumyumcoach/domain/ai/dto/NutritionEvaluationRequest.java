package com.yumyumcoach.domain.ai.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class NutritionEvaluationRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate anchorDate;
}
