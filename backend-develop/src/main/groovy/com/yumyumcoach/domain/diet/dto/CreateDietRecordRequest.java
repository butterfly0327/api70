package com.yumyumcoach.domain.diet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDietRecordRequest {

    // MyBatis generated keys 채우기용 (keyProperty="id")
    @Setter
    private Long id; // insert 시 생성된 PK를 MyBatis가 채우기 위함

    @NotNull
    private LocalDateTime recordedAt;

    @NotBlank
    private String mealType; // BREAKFAST/LUNCH/DINNER/SNACK...

    @NotEmpty
    @Valid
    private List<CreateDietFoodRequest> items;

    private String imageUrl; // CloudFront 고정 URL 저장

}


