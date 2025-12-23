package com.yumyumcoach.domain.auth.mapper;

import com.yumyumcoach.domain.auth.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/*
login 로직에서 필요한 email 로 회원찾기

성공 시: 해당 Account 반환
실패 시: null 반환
 */

@Mapper
public interface AccountMapper {
    Account findByEmail(@Param("email") String email);

    // 이메일 중복 확인
    boolean existsByEmail(@Param("email") String email);

    // 닉네임(username) 중복 확인
    boolean existsByUsername(@Param("username") String username);

    // 닉네임 변경
    int updateUsername(@Param("email") String email,
                       @Param("username") String username);

    // 신규 계정 저장
    void insertNewAccount(Account account);

    // 탈퇴하려는 회원의 계정 삭제
    void deleteAccountByEmail(@Param("email") String email);

    /**
     * userId(id)로 이메일 조회
     * - API PathVariable(userId) -> 내부 email 변환에 사용
     */
    String findEmailById(@Param("id") Long id);

    /**
     * 이메일로 userId(id) 조회
     * - 응답에 userId가 필요할 때 사용
     */
    Long findIdByEmail(@Param("email") String email);
}
