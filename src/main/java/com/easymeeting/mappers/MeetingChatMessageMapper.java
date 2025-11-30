package com.easymeeting.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MeetingChatMessageMapper<T, P> extends BaseMapperTableSplit {

    T selectByMessageId(@Param("messageId") Long messageId, @Param("tableName") String tableName);

    List<T> selectListByParam(@Param("param") P param, @Param("tableName") String tableName);

    Integer selectCountByParam(@Param("param") P param, @Param("tableName") String tableName);

    int insert(@Param("bean") T bean, @Param("tableName") String tableName);

    int insertBatch(@Param("list") List<T> list, @Param("tableName") String tableName);

    int insertOrUpdateBatch(@Param("list") List<T> list, @Param("tableName") String tableName);

    int insertOrUpdate(@Param("bean") T bean, @Param("tableName") String tableName);

    int updateByParam(@Param("bean") T bean, @Param("param") P param, @Param("tableName") String tableName);

    int updateByMessageId(@Param("bean") T bean, @Param("messageId") Long messageId, @Param("tableName") String tableName);

    int deleteByParam(@Param("param") P param, @Param("tableName") String tableName);

    int deleteByMessageId(@Param("messageId") Long messageId, @Param("tableName") String tableName);
}

