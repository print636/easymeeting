package com.easymeeting.service;

import com.easymeeting.entity.po.UserContactApply;
import com.easymeeting.entity.query.UserContactApplyQuery;
import com.easymeeting.entity.vo.PaginationResultVO;

import java.util.List;

public interface UserContactApplyService {

    List<UserContactApply> findListByParam(UserContactApplyQuery param);

    Integer findCountByParam(UserContactApplyQuery param);

    PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery param);

    Integer add(UserContactApply bean);

    Integer addBatch(List<UserContactApply> listBean);

    Integer addOrUpdateBatch(List<UserContactApply> listBean);

    Integer updateByParam(UserContactApply bean, UserContactApplyQuery param);

    Integer deleteByParam(UserContactApplyQuery param);

    UserContactApply getUserContactApplyByApplyId(Integer applyId);

    UserContactApply getUserContactApplyByApplyUserIdAndReceiveUserId(String applyUserId, String receiveUserId);

    Integer updateUserContactApplyByApplyId(UserContactApply bean, Integer applyId);

    Integer updateUserContactApplyByApplyUserIdAndReceiveUserId(UserContactApply bean, String applyUserId, String receiveUserId);

    Integer deleteUserContactApplyByApplyId(Integer applyId);

    Integer deleteUserContactApplyByApplyUserIdAndReceiveUserId(String applyUserId, String receiveUserId);

    Integer saveUserContactApply(UserContactApply userContactApply);

    void dealWithApply(String applyUserId, String userId, String nickName, Integer status);
}

