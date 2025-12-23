package com.yumyumcoach.domain.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiDetectionResult {
    @JsonProperty("class_id")
    private Integer classId;
    private String label;
    private Double confidence;
    private AiBoundingBox box;
}
