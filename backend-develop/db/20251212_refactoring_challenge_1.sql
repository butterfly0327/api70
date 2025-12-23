USE yumyumcoach;

CREATE TABLE IF NOT EXISTS challenge_rules (
                                               challenge_id          BIGINT UNSIGNED NOT NULL,
                                               difficulty_code       VARCHAR(255) NOT NULL,  -- BEGINNER / INTERMEDIATE / ADVANCED
                                               required_success_days INT NOT NULL,           -- 최소 성공해야 하는 일수
                                               daily_target_value    DOUBLE DEFAULT NULL,    -- 하루 목표 값 (단순 일수 기준이면 NULL)

                                               PRIMARY KEY (challenge_id, difficulty_code),
                                               CONSTRAINT fk_challenge_rules_challenge
                                                   FOREIGN KEY (challenge_id) REFERENCES challenges(id),
                                               CONSTRAINT fk_challenge_rules_difficulty
                                                   FOREIGN KEY (difficulty_code) REFERENCES challenge_difficulties(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE challenges
    DROP FOREIGN KEY fk_challenges_type;

ALTER TABLE challenges
    DROP COLUMN type_code;

ALTER TABLE challenges
    DROP FOREIGN KEY fk_challenges_difficulty;

ALTER TABLE challenges
    DROP COLUMN difficulty_code;

DROP TABLE IF EXISTS challenge_types;

ALTER TABLE challenges
    ADD COLUMN challenge_type VARCHAR(20) NOT NULL DEFAULT 'PUBLIC';

ALTER TABLE challenge_participants
    ADD COLUMN success_days INT NOT NULL DEFAULT 0
        AFTER progress_percentage;

