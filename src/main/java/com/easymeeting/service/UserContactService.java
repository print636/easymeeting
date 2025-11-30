package com.easymeeting.service;

import com.easymeeting.entity.po.UserContact;
import com.easymeeting.entity.query.UserContactQuery;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.entity.vo.UserInfoVO4Search;

import java.util.List;

public interface UserContactService {

    List<UserContact> findListByParam(UserContactQuery param);

    Integer findCountByParam(UserContactQuery param);

    PaginationResultVO<UserContact> findListByPage(UserContactQuery param);

    Integer add(UserContact bean);

    Integer addBatch(List<UserContact> listBean);

    Integer addOrUpdateBatch(List<UserContact> listBean);

    Integer updateByParam(UserContact bean, UserContactQuery param);

    Integer deleteByParam(UserContactQuery param);

    UserContact getUserContactByUserIdAndContactId(String userId, String contactId);

    Integer updateUserContactByUserIdAndContactId(UserContact bean, String userId, String contactId);

    Integer deleteUserContactByUserIdAndContactId(String userId, String contactId);

    UserInfoVO4Search searchContact(String userId, String contactId);

    void delContact(String userId, String contactId, Integer status);
}



