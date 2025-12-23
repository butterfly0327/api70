package com.yumyumcoach.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 사용자 프로필 엔티티 (profiles 테이블 매핑).
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    /**
     * 계정 이메일 (PK/FK: accounts.email)
     */
    private String email;

    /**
     * 한 줄 소개
     */
    private String introduction;

    /**
     * 내 프로필 사진 - DB : profile_image_url
     */
    private String profileImageUrl;

    /**
     * 내 생년월일 - DB : birth_date
     */
    private LocalDate birthDate;

    /**
     * 키(cm)
     */
    private Double height;

    /**
     * 현재 체중(kg) - DB: current_weight
     */
    private Double currentWeight;

    /**
     * 목표 체중(kg) - DB: target_weight
     */
    private Double targetWeight;

    /**
     * 당뇨 여부 - DB: has_diabetes
     */
    private Boolean hasDiabetes;

    /**
     * 고혈압 여부 - DB: has_hypertension
     */
    private Boolean hasHypertension;

    /**
     * 고지혈증 여부 - DB: has_hyperlipidemia
     */
    private Boolean hasHyperlipidemia;

    /**
     * 기타 질환 - DB: other_disease (TEXT)
     */
    private String otherDisease;

    /**
     * 목표(자유 텍스트) - DB: goal
     */
    private String goal;

    /**
     * 활동량 레벨(예: LOW/MODERATE/HIGH) - DB: activity_level
     */
    private String activityLevel;

    /**
     * 대표 타이틀 ID (FK: titles.id) - DB: display_title_id
     */
    private Long displayTitleId;
}