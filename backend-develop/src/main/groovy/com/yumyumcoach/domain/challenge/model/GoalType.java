package com.yumyumcoach.domain.challenge.model;

public enum GoalType {
    DAY_COUNT_SIMPLE,        // 이달 N일 이상 기록
    PROTEIN_PER_DAY,         // 하루 단백질 Xg 이상인 날 N일 이상
    EXERCISE_MINUTES_PER_DAY; // 하루 운동 Y분 이상인 날 N일 이상

    public static GoalType from(String value) {
        return GoalType.valueOf(value);
    }

    public String getCode() {
        return this.name();
    }
}
