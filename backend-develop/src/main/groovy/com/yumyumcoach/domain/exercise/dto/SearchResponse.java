package com.yumyumcoach.domain.exercise.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse {
    private int page;
    private int size;
    private int total;

    private List<SearchDto> result;
}
