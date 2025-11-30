package com.easymeeting.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MeetingMemberMapper<T, P> extends BaseMapper {

    List<T> selectListByParam(P param);

    Integer selectCountByParam(P param);

    int insert(T bean);

    int insertBatch(@Param("list") List<T> list);

    int insertOrUpdateBatch(@Param("list") List<T> list);

    int insertOrUpdate(T bean);

    int updateByParam(@Param("bean") T bean, @Param("param") P param);

    int deleteByParam(P param);
}

