package com.easymeeting.mappers;

import com.easymeeting.entity.vo.UserInfoVO4Search;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserContactMapper<T, P> extends BaseMapper {

    T selectByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);

    List<T> selectListByParam(P param);

    Integer selectCountByParam(P param);

    int insert(T bean);

    int insertBatch(@Param("list") List<T> list);

    int insertOrUpdateBatch(@Param("list") List<T> list);

    int insertOrUpdate(T bean);

    int updateByParam(@Param("bean") T bean, @Param("param") P param);

    int updateByUserIdAndContactId(@Param("bean") T bean, @Param("userId") String userId, @Param("contactId") String contactId);

    int deleteByParam(P param);

    int deleteByUserIdAndContactId(@Param("userId") String userId, @Param("contactId") String contactId);


}



