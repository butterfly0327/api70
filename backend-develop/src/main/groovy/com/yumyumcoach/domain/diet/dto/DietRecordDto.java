package com.yumyumcoach.domain.diet.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietRecordDto {

    private Long id;
    private LocalDateTime recordedAt;
    private String mealType;
    private String imageUrl;
    private List<DietFoodDto> items;
}


