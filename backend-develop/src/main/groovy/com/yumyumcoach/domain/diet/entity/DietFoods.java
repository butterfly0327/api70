package com.yumyumcoach.domain.diet.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DietFoods {

    private Long id;

    private DietRecords dietId;   // FK(식단)
    private Integer orderIndex;

    private Foods foodId;         // FK(음식)
    private Double weight;

    @Builder
    public DietFoods(DietRecords dietId, Integer orderIndex, Foods foodId, Double weight) {
        this.dietId = dietId;
        this.orderIndex = orderIndex;
        this.foodId = foodId;
        this.weight = weight;
    }
}
