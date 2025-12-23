package com.yumyumcoach.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMyHealthInfoRequest {

    private LocalDate birthDate;       // profiles.birth_date (optional)
    private Double height;             // profiles.height (optional)
    private Double weight;             // profiles.current_weight (optional)
    private Double goalWeight;         // profiles.target_weight (optional)

    private Boolean hasDiabetes;       // profiles.has_diabetes (optional)
    private Boolean hasHypertension;   // profiles.has_hypertension (optional)
    private Boolean hasHyperlipidemia; // profiles.has_hyperlipidemia (optional)
    private String otherDisease;       // profiles.other_disease (optional)

    private String goal;               // profiles.goal (optional)
    private String activityLevel;      // profiles.activity_level (optional)

    public boolean hasAnyValue() {
        return birthDate != null ||
                height != null ||
                weight != null ||
                goalWeight != null ||
                hasDiabetes != null ||
                hasHypertension != null ||
                hasHyperlipidemia != null ||
                otherDisease != null ||
                goal != null ||
                activityLevel != null;
    }
}
