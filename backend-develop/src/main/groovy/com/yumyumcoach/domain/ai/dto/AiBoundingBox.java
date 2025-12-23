package com.yumyumcoach.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiBoundingBox {
    private Double x1;
    private Double y1;
    private Double x2;
    private Double y2;
}
