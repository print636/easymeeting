package com.easymeeting.service;

import com.easymeeting.entity.po.UserInfo;
import com.easymeeting.entity.query.UserInfoQuery;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.entity.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserInfoService {

    //根据条件查询列表
    List<UserInfo> findListByParam(UserInfoQuery param);

    //根据条件查询列表
    Integer findCountByParam(UserInfoQuery param);

    //分页查询
    PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param);

    //新增
    Integer add(UserInfo bean);
    //批量新增
    Integer addBatch(List<UserInfo> listBean);

    //批量新增/修改
    Integer addOrUpdateBatch(List<UserInfo> listBean);

    //多条件更新
    Integer updateByParam(UserInfo bean, UserInfoQuery param);

    //多条件删除
    Integer deleteByParam(UserInfoQuery param);

    //根据id查询
    UserInfo getUserInfoByUserId(String userId);

    //根据id修改
    Integer updateUserInfoByUserId(UserInfo bean,String userId);

    //根据UserId删除
    Integer deleteUserInfoByUserId(String userId);

    //根据Email修改
    Integer updateUserInfoByEmail(UserInfo bean,String email);

    //根据Email删除
    Integer deleteUserInfoByEmail(String email);
    void register(String email,String nickName,String password);

    UserInfoVO login(String email,String password);

    /**
     * 管理员修改用户状态（启用 / 禁用）。
     */
    void updateUserStatus(Integer status, String userId);

    /**
     * 强制用户下线：更新离线时间，并在用户在线时通过 WebSocket 推送强制下线消息。
     */
    void forceOffline(String userId);

    void updateUserInfo(MultipartFile avatar, UserInfo userInfo) throws IOException;

    void updatePassword(String userId, String oldPassword, String newPassword);
}
