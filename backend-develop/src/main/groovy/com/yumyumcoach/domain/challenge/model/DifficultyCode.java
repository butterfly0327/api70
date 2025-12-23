package com.yumyumcoach.domain.challenge.model;

public enum DifficultyCode {
    BEGINNER,      // 초급
    INTERMEDIATE,  // 중급
    ADVANCED;      // 고급

    public static DifficultyCode from(String value) {
        return DifficultyCode.valueOf(value);
    }

    public String getCode() {
        return this.name();
    }
}
