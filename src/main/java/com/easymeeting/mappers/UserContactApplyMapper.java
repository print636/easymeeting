package com.easymeeting.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserContactApplyMapper<T, P> extends BaseMapper {

    T selectByApplyId(@Param("applyId") Integer applyId);

    T selectByApplyUserIdAndReceiveUserId(@Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId);

    List<T> selectListByParam(P param);

    Integer selectCountByParam(P param);

    int insert(T bean);

    int insertBatch(@Param("list") List<T> list);

    int insertOrUpdateBatch(@Param("list") List<T> list);

    int insertOrUpdate(T bean);

    int updateByParam(@Param("bean") T bean, @Param("param") P param);

    int updateByApplyId(@Param("bean") T bean, @Param("applyId") Integer applyId);

    int updateByApplyUserIdAndReceiveUserId(@Param("bean") T bean, @Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId);

    int deleteByParam(P param);

    int deleteByApplyId(@Param("applyId") Integer applyId);

    int deleteByApplyUserIdAndReceiveUserId(@Param("applyUserId") String applyUserId, @Param("receiveUserId") String receiveUserId);
}

