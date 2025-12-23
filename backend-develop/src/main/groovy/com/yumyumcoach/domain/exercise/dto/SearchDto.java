package com.yumyumcoach.domain.exercise.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDto {
    @NotBlank
    private String name;
    private double met;
    @NotBlank
    private String type;
}
