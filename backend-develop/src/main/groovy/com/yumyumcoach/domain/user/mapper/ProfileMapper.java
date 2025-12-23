package com.yumyumcoach.domain.user.mapper;

import com.yumyumcoach.domain.user.dto.UserProfileRow;
import com.yumyumcoach.domain.user.entity.Profile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfileMapper {

    /**
     * 프로필 조회 (마이페이지/수정 후 응답 구성용)
     */
    Profile findByEmail(@Param("email") String email);

    /**
     * profiles row 생성 (email만)
     * - 회원가입 직후 1회 호출용
     * - 중복 호출돼도 안전하게 하려면 XML에서 INSERT IGNORE 사용
     * @return insert된 행 수(1 또는 0)
     */
    int insertEmpty(@Param("email") String email);

    /**
     * 기본정보 수정 (소개/프로필이미지)
     * @return 수정된 행 수(0이면 대상 없음)
     */
    int updateBasic(Profile profile);

    /**
     * 건강정보 수정 (온보딩 포함)
     */
    void updateHealth(Profile profile);

    /**
     * 대표 타이틀 설정
     * @return 수정된 행 수(0이면 대상 없음)
     */
    int updateDisplayTitle(@Param("email") String email,
                           @Param("displayTitleId") Long displayTitleId);

    /**
     * 상대방 프로필 조회
     */
    UserProfileRow findUserProfileRow(@Param("userId") Long userId);
}

