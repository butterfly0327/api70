-- 신규 AI 및 통계 관련 테이블 정의

USE yumyumcoach;

CREATE TABLE IF NOT EXISTS ai_meal_plans (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    target_date DATE NOT NULL,
    weekday_kr VARCHAR(20) DEFAULT NULL,
    breakfast_menu TEXT,
    breakfast_calories DECIMAL(10,2) NOT NULL DEFAULT 0,
    breakfast_comment VARCHAR(255) DEFAULT NULL,
    lunch_menu TEXT,
    lunch_calories DECIMAL(10,2) NOT NULL DEFAULT 0,
    lunch_comment VARCHAR(255) DEFAULT NULL,
    dinner_menu TEXT,
    dinner_calories DECIMAL(10,2) NOT NULL DEFAULT 0,
    dinner_comment VARCHAR(255) DEFAULT NULL,
    total_calories DECIMAL(10,2) NOT NULL DEFAULT 0,
    prompt_context TEXT,
    raw_response LONGTEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_ai_meal_plan_email_date (email, target_date),
    CONSTRAINT fk_ai_meal_plan_email FOREIGN KEY (email) REFERENCES accounts(email) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS ai_nutrition_week_reviews (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    week_start_date DATE NOT NULL,
    week_end_date DATE NOT NULL,
    evaluated_until_date DATE NOT NULL,
    carbohydrate_status VARCHAR(30) NOT NULL DEFAULT 'UNKNOWN',
    protein_status VARCHAR(30) NOT NULL DEFAULT 'UNKNOWN',
    fat_status VARCHAR(30) NOT NULL DEFAULT 'UNKNOWN',
    calorie_status VARCHAR(30) NOT NULL DEFAULT 'UNKNOWN',
    summary_text TEXT,
    prompt_context TEXT,
    raw_response LONGTEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_ai_nutrition_week_email (email, week_start_date),
    CONSTRAINT fk_ai_nutrition_email FOREIGN KEY (email) REFERENCES accounts(email) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS ai_exercise_week_reviews (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    week_start_date DATE NOT NULL,
    week_end_date DATE NOT NULL,
    evaluated_until_date DATE NOT NULL,
    volume_status VARCHAR(30) NOT NULL DEFAULT 'UNKNOWN',
    recommendation TEXT,
    summary_text TEXT,
    prompt_context TEXT,
    raw_response LONGTEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_ai_exercise_week_email (email, week_start_date),
    CONSTRAINT fk_ai_exercise_email FOREIGN KEY (email) REFERENCES accounts(email) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
