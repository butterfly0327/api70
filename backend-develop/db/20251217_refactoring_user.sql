USE yumyumcoach;

-- 1) URL에 이메일을 노출하지 않기 위해 accounts에 숫자 식별자(id) 추가
--    - 기존 PK(email) 및 기존 FK 구조는 유지
ALTER TABLE accounts
    ADD COLUMN id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    ADD UNIQUE KEY uq_accounts_id (id);

-- 2) 닉네임 중복 방지를 DB 레벨에서 보장 (409 처리 근거)
ALTER TABLE accounts
    ADD UNIQUE KEY uq_accounts_username (username);

-- 3) 실명 필드를 쓰지 않기로 결정하여 profiles.name 제거
--    - 마이페이지의 email/username은 accounts에서 조회
ALTER TABLE profiles
    DROP COLUMN name;

-- 4) 프로필 사진 url 및 생년월일을 저장할 컬럼 추가
ALTER TABLE profiles
    ADD COLUMN profile_image_url VARCHAR(1024) DEFAULT NULL AFTER introduction,
    ADD COLUMN birth_date DATE DEFAULT NULL AFTER profile_image_url;
