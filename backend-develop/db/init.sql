CREATE DATABASE IF NOT EXISTS yumyumcoach
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE yumyumcoach;

-- 1) accounts (최종: id 추가 + username UNIQUE)
CREATE TABLE IF NOT EXISTS accounts (
  email VARCHAR(255) NOT NULL,
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY (email),
  UNIQUE KEY uq_accounts_id (id),
  UNIQUE KEY uq_accounts_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 2) refresh_tokens (중복 제거, 1회만)
CREATE TABLE IF NOT EXISTS refresh_tokens (
  email VARCHAR(255) NOT NULL,
  token_hash CHAR(64) NOT NULL,
  expires_at DATETIME NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (email),
  UNIQUE KEY uk_refresh_email_hash (email, token_hash),
  KEY idx_refresh_expires (expires_at),
  CONSTRAINT fk_refresh_email
    FOREIGN KEY (email) REFERENCES accounts(email) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 3) titles
CREATE TABLE IF NOT EXISTS titles (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) DEFAULT NULL,
  icon_emoji VARCHAR(32) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 4) profiles (최종: name 삭제 + profile_image_url/birth_date 추가)
CREATE TABLE IF NOT EXISTS profiles (
  email VARCHAR(255) NOT NULL,
  introduction VARCHAR(255) DEFAULT NULL,
  profile_image_url VARCHAR(1024) DEFAULT NULL,
  birth_date DATE DEFAULT NULL,

  height DOUBLE DEFAULT NULL,
  current_weight DOUBLE DEFAULT NULL,
  target_weight DOUBLE DEFAULT NULL,

  has_diabetes TINYINT DEFAULT NULL,
  has_hypertension TINYINT DEFAULT NULL,
  has_hyperlipidemia TINYINT DEFAULT NULL,

  other_disease TEXT DEFAULT NULL,
  goal VARCHAR(255) DEFAULT NULL,
  activity_level VARCHAR(255) DEFAULT NULL,

  display_title_id BIGINT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (email),
  CONSTRAINT fk_profiles_account
    FOREIGN KEY (email) REFERENCES accounts(email),
  CONSTRAINT fk_profiles_display_title
    FOREIGN KEY (display_title_id) REFERENCES titles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 5) posts
CREATE TABLE IF NOT EXISTS posts (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  author_email VARCHAR(255) NOT NULL,
  title VARCHAR(255) NOT NULL,
  category VARCHAR(255) DEFAULT NULL,
  content TEXT DEFAULT NULL,
  created_at DATETIME NOT NULL,
  likes INT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT fk_posts_author
    FOREIGN KEY (author_email) REFERENCES accounts(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 6) post_images
CREATE TABLE IF NOT EXISTS post_images (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  post_id BIGINT UNSIGNED NOT NULL,
  image_url VARCHAR(1024) NOT NULL,
  order_index INT NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  UNIQUE KEY uq_post_images_post_order (post_id, order_index),
  CONSTRAINT fk_post_images_post
    FOREIGN KEY (post_id) REFERENCES posts(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 7) post_comments
CREATE TABLE IF NOT EXISTS post_comments (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  post_id BIGINT UNSIGNED NOT NULL,
  author_email VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  created_at DATETIME NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_post_comments_post
    FOREIGN KEY (post_id) REFERENCES posts(id),
  CONSTRAINT fk_post_comments_author
    FOREIGN KEY (author_email) REFERENCES accounts(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 8) post_likes (중복 제거, 최종 1회만)
CREATE TABLE IF NOT EXISTS post_likes (
  post_id BIGINT UNSIGNED NOT NULL,
  email VARCHAR(255) NOT NULL,
  created_at DATETIME NOT NULL,
  PRIMARY KEY (post_id, email),
  KEY idx_post_likes_email_created_at (email, created_at),
  CONSTRAINT fk_post_likes_post
    FOREIGN KEY (post_id) REFERENCES posts(id),
  CONSTRAINT fk_post_likes_account
    FOREIGN KEY (email) REFERENCES accounts(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 9) diet_records
CREATE TABLE IF NOT EXISTS diet_records (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  recorded_at DATETIME NOT NULL,
  meal_type VARCHAR(255) NOT NULL,
  image_url VARCHAR(1024) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_diet_records_account
    FOREIGN KEY (email) REFERENCES accounts(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 10) foods
CREATE TABLE IF NOT EXISTS foods (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  carbohydrate DOUBLE DEFAULT NULL,
  protein DOUBLE DEFAULT NULL,
  fat DOUBLE DEFAULT NULL,
  calories DOUBLE DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 11) diet_foods (최종: weight -> serve_count)
CREATE TABLE IF NOT EXISTS diet_foods (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  diet_id BIGINT UNSIGNED NOT NULL,
  order_index INT NOT NULL,
  food_id BIGINT UNSIGNED NOT NULL,
  serve_count DOUBLE NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  UNIQUE KEY uq_diet_foods_diet_order (diet_id, order_index),
  CONSTRAINT fk_diet_foods_record
    FOREIGN KEY (diet_id) REFERENCES diet_records(id),
  CONSTRAINT fk_diet_foods_food
    FOREIGN KEY (food_id) REFERENCES foods(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 12) exercises
CREATE TABLE IF NOT EXISTS exercises (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  met DOUBLE NOT NULL,
  intensity_level VARCHAR(255) NOT NULL,
  type VARCHAR(255) NOT NULL,
  description TEXT DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 13) exercise_records
CREATE TABLE IF NOT EXISTS exercise_records (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  exercise_id BIGINT UNSIGNED NOT NULL,
  recorded_at DATETIME NOT NULL,
  duration_minutes DOUBLE NOT NULL,
  calories DOUBLE NOT NULL,
  PRIMARY KEY (id),
  KEY idx_exercise_records_email_recorded_at (email, recorded_at),
  CONSTRAINT fk_exercise_records_account
    FOREIGN KEY (email) REFERENCES accounts(email),
  CONSTRAINT fk_exercise_records_exercise
    FOREIGN KEY (exercise_id) REFERENCES exercises(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 14) challenge_difficulties (남아있음: challenge_rules/participants가 사용)
CREATE TABLE IF NOT EXISTS challenge_difficulties (
  code VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT DEFAULT NULL,
  PRIMARY KEY (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 15) challenges (최종: type_code/difficulty_code 제거 + recruit/end/goal_type/challenge_type 반영)
CREATE TABLE IF NOT EXISTS challenges (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  reward_title_id BIGINT UNSIGNED DEFAULT NULL,

  name VARCHAR(255) NOT NULL,
  short_description VARCHAR(255) NOT NULL,
  goal_summary VARCHAR(255) NOT NULL,
  rule_description TEXT DEFAULT NULL,

  image_url VARCHAR(1024) DEFAULT NULL,

  recruit_start_date DATE NOT NULL,
  recruit_end_date DATE NOT NULL,

  start_date DATE NOT NULL,
  end_date DATE NOT NULL,

  is_active TINYINT NOT NULL DEFAULT 1,
  goal_type VARCHAR(50) NOT NULL DEFAULT 'DAY_COUNT_SIMPLE',
  challenge_type VARCHAR(20) NOT NULL DEFAULT 'PUBLIC',

  PRIMARY KEY (id),
  CONSTRAINT fk_challenges_reward_title
    FOREIGN KEY (reward_title_id) REFERENCES titles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 16) challenge_rules  ✅ (오타/문법만 수정)
CREATE TABLE IF NOT EXISTS challenge_rules (
  challenge_id BIGINT UNSIGNED NOT NULL,
  difficulty_code VARCHAR(255) NOT NULL,
  required_success_days INT NOT NULL,
  daily_target_value DOUBLE DEFAULT NULL,
  reward_title_id BIGINT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (challenge_id, difficulty_code),
  CONSTRAINT fk_challenge_rules_challenge
    FOREIGN KEY (challenge_id) REFERENCES challenges(id),
  CONSTRAINT fk_challenge_rules_difficulty
    FOREIGN KEY (difficulty_code) REFERENCES challenge_difficulties(code),
  CONSTRAINT fk_challenge_rules_reward_title
    FOREIGN KEY (reward_title_id) REFERENCES titles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 17) challenge_participants (최종: difficulty/required/daily + success_days)
CREATE TABLE IF NOT EXISTS challenge_participants (
  challenge_id BIGINT UNSIGNED NOT NULL,
  email VARCHAR(255) NOT NULL,
  joined_at DATETIME NOT NULL,

  status VARCHAR(255) NOT NULL DEFAULT 'joined',

  difficulty_code VARCHAR(255) NOT NULL,
  required_success_days INT NOT NULL,
  daily_target_value DOUBLE DEFAULT NULL,

  progress_percentage DOUBLE NOT NULL DEFAULT 0,
  success_days INT NOT NULL DEFAULT 0,

  last_evaluated_at DATETIME DEFAULT NULL,
  completed_at DATETIME DEFAULT NULL,

  PRIMARY KEY (challenge_id, email),
  CONSTRAINT fk_challenge_participants_challenge
    FOREIGN KEY (challenge_id) REFERENCES challenges(id),
  CONSTRAINT fk_challenge_participants_account
    FOREIGN KEY (email) REFERENCES accounts(email),
  CONSTRAINT fk_challenge_participants_difficulty
    FOREIGN KEY (difficulty_code) REFERENCES challenge_difficulties(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 18) account_titles
CREATE TABLE IF NOT EXISTS account_titles (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  title_id BIGINT UNSIGNED NOT NULL,
  email VARCHAR(255) NOT NULL,
  obtained_at DATETIME NOT NULL,
  source_challenge_id BIGINT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_account_titles_email_title (email, title_id),
  CONSTRAINT fk_account_titles_account
    FOREIGN KEY (email) REFERENCES accounts(email),
  CONSTRAINT fk_account_titles_title
    FOREIGN KEY (title_id) REFERENCES titles(id),
  CONSTRAINT fk_account_titles_challenge
    FOREIGN KEY (source_challenge_id) REFERENCES challenges(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 19) follows
CREATE TABLE IF NOT EXISTS follows (
  follower_email VARCHAR(255) NOT NULL,
  followee_email VARCHAR(255) NOT NULL,
  followed_at DATETIME NOT NULL,
  PRIMARY KEY (follower_email, followee_email),
  CONSTRAINT fk_follows_follower
    FOREIGN KEY (follower_email) REFERENCES accounts(email),
  CONSTRAINT fk_follows_followee
    FOREIGN KEY (followee_email) REFERENCES accounts(email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 20) ai_meal_plans
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

-- 21) ai_nutrition_week_reviews
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

-- 22) ai_exercise_week_reviews
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