ALTER TABLE titles
    ADD COLUMN icon_emoji VARCHAR(16) DEFAULT NULL AFTER id;

ALTER TABLE challenge_rules
    ADD COLUMN reward_title_id BIGINT UNSIGNED NOT NULL AFTER difficulty_code;

ALTER TABLE challenge_rules
    ADD CONSTRAINT fk_challenge_rules_reward_title
        FOREIGN KEY (reward_title_id) REFERENCES titles(id);