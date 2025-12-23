package com.yumyumcoach.domain.user.mapper;

import com.yumyumcoach.domain.user.dto.MyFollowersResponse;
import com.yumyumcoach.domain.user.dto.MyFollowingsResponse;
import com.yumyumcoach.domain.user.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowMapper {

    /**
     * 팔로우 관계 존재 여부
     */
    boolean exists(@Param("followerEmail") String followerEmail,
                   @Param("followeeEmail") String followeeEmail);

    /**
     * 팔로우 추가
     */
    void insert(Follow follow);

    /**
     * 팔로우 취소
     * @return 삭제된 행 수 (0이면 팔로우 관계 없음)
     */
    int delete(@Param("followerEmail") String followerEmail,
               @Param("followeeEmail") String followeeEmail);

    /**
     * 팔로워 수 (나를 팔로우하는 사람 수)
     */
    long countFollowers(@Param("email") String email);

    /**
     * 팔로잉 수 (내가 팔로우하는 사람 수)
     */
    long countFollowings(@Param("email") String email);

    /**
     * 내가 팔로우하는 유저 목록
     */
    List<MyFollowingsResponse.User> findMyFollowings(@Param("email") String email);

    /**
     * 나를 팔로우하는 유저 목록
     */
    List<MyFollowersResponse.User> findMyFollowers(@Param("email") String email);
}

