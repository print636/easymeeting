package com.easymeeting.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper<T, P> extends BaseMapper {

    // 根据邮箱查询
    T selectByEmail(@Param("email") String email);
    
    // 根据用户ID查询
    T selectByUserId(@Param("userId") String userId);
    
    // 根据条件查询列表
    List<T> selectListByParam(P param);
    
    // 根据条件查询总数
    Integer selectCountByParam(P param);
    
    // 插入单条记录
    int insert(T userInfo);
    
    // 批量插入
    int insertBatch(@Param("list") List<T> list);
    
    // 批量插入或更新
    int insertOrUpdateBatch(@Param("list") List<T> list);
    
    // 插入单条插入或更新
    int insertOrUpdate(T bean);
    
    // 根据条件更新
    int updateByParam(@Param("bean") T bean, @Param("param") P param);
    
    // 根据用户ID更新
    int updateByUserId(@Param("bean") T bean, @Param("userId") String userId);
    
    // 根据邮箱更新
    int updateByEmail(@Param("bean") T bean, @Param("email") String email);
    
    // 根据条件删除
    int deleteByParam(P param);
    
    // 根据用户ID删除
    int deleteByUserId(@Param("userId") String userId);
    
    // 根据邮箱删除
    int deleteByEmail(@Param("email") String email);
}
