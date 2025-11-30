package com.easymeeting.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MeetingReserveMapper<T, P> extends BaseMapper {

    T selectByMeetingId(@Param("meetingId") String meetingId);

    List<T> selectListByParam(P param);

    Integer selectCountByParam(P param);

    int insert(T bean);

    int insertBatch(@Param("list") List<T> list);

    int insertOrUpdateBatch(@Param("list") List<T> list);

    int insertOrUpdate(T bean);

    int updateByParam(@Param("bean") T bean, @Param("param") P param);

    int updateByMeetingId(@Param("bean") T bean, @Param("meetingId") String meetingId);

    int deleteByParam(P param);

    int deleteByMeetingId(@Param("meetingId") String meetingId);
}

