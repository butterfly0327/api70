ALTER TABLE challenges
    ADD COLUMN recruit_start_date DATE NOT NULL AFTER image_url,
    ADD COLUMN recruit_end_date DATE NOT NULL AFTER recruit_start_date,
    ADD COLUMN end_date DATE NOT NULL AFTER start_date,
    ADD COLUMN goal_type VARCHAR(50) NOT NULL DEFAULT 'DAY_COUNT_SIMPLE' AFTER is_active,
    DROP COLUMN duration_days;

ALTER TABLE challenge_participants
    ADD COLUMN difficulty_code VARCHAR(255) NOT NULL AFTER status,
    ADD COLUMN required_success_days INT NOT NULL AFTER difficulty_code,
    ADD COLUMN daily_target_value DOUBLE DEFAULT NULL AFTER required_success_days;
