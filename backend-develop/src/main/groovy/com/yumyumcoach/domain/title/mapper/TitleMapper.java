package com.yumyumcoach.domain.title.mapper;

import com.yumyumcoach.domain.title.dto.MyTitleItemResponse;
import com.yumyumcoach.domain.title.dto.MyTitleResponse;
import com.yumyumcoach.domain.user.dto.MyPageResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TitleMapper {

    /**
     * 내가 보유한 타이틀인지 여부
     * - 대표 타이틀 설정 가능 여부 검증용
     */
    boolean ownsTitle(@Param("email") String email,
                      @Param("titleId") Long titleId);

    /**
     * 현재 대표 타이틀 조회
     * - 대표 타이틀이 없으면 currentTitleId/currentTitleName이 null일 수 있음
     */
    MyTitleResponse findCurrentTitle(@Param("email") String email);

    /**
     * 내가 보유한 타이틀 목록 조회
     * - account_titles + titles 조인
     */
    List<MyTitleItemResponse> findMyTitles(@Param("email") String email);
}

