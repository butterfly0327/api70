USE yumyumcoach;

-- 공통: 특정 테이블의 email FK를 찾아 DROP 후 CASCADE로 재생성
DELIMITER $$

CREATE PROCEDURE reset_email_fk(
  IN p_table VARCHAR(64),
  IN p_column VARCHAR(64),
  IN p_fkname VARCHAR(64)
)
BEGIN
  DECLARE v_fk VARCHAR(64);

  SELECT CONSTRAINT_NAME INTO v_fk
  FROM information_schema.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = p_table
    AND COLUMN_NAME = p_column
    AND REFERENCED_TABLE_NAME = 'accounts'
  LIMIT 1;

  IF v_fk IS NOT NULL THEN
    SET @s1 = CONCAT('ALTER TABLE ', p_table, ' DROP FOREIGN KEY ', v_fk);
    PREPARE stmt1 FROM @s1;
    EXECUTE stmt1;
    DEALLOCATE PREPARE stmt1;
  END IF;

  SET @s2 = CONCAT(
    'ALTER TABLE ', p_table,
    ' ADD CONSTRAINT ', p_fkname,
    ' FOREIGN KEY (', p_column, ') REFERENCES accounts(email) ON DELETE CASCADE'
  );
  PREPARE stmt2 FROM @s2;
  EXECUTE stmt2;
  DEALLOCATE PREPARE stmt2;
END$$

DELIMITER ;

-- 적용 대상들
CALL reset_email_fk('account_titles',        'email',          'fk_account_titles_account');
CALL reset_email_fk('challenge_participants','email',          'fk_challenge_participants_account');
CALL reset_email_fk('diet_records',           'email',          'fk_diet_records_account');
CALL reset_email_fk('exercise_records',       'email',          'fk_exercise_records_account');
CALL reset_email_fk('post_likes',              'email',          'fk_post_likes_account');
CALL reset_email_fk('profiles',                'email',          'fk_profiles_account');
CALL reset_email_fk('refresh_tokens',          'email',          'fk_refresh_email');

-- follows는 email 컬럼이 2개라 별도 처리
CALL reset_email_fk('follows', 'followee_email', 'fk_follows_followee');
CALL reset_email_fk('follows', 'follower_email', 'fk_follows_follower');

-- 정리
DROP PROCEDURE reset_email_fk;


INSERT INTO accounts (email, username, password)
VALUES ('deleted@system','탈퇴한 사용자', 'dummy')
ON DUPLICATE KEY UPDATE
username = VALUES(username),
password = VALUES(password);

INSERT INTO profiles (email, introduction)
VALUES ('deleted@system', '탈퇴한 회원입니다.')
ON DUPLICATE KEY UPDATE introduction = VALUES(introduction);