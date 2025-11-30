package com.easymeeting.controller;

import com.easymeeting.annotation.GlobalInterceptor;
import com.easymeeting.entity.query.UserInfoQuery;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.entity.vo.ResponseVO;
import com.easymeeting.mappers.UserInfoMapper;
import com.easymeeting.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/admin")
@Validated
@Slf4j
public class AdminController extends  ABaseController{

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/loadUserList")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO loadUserList(String nickName, Integer pageNo, Integer pageSize) {
        UserInfoQuery userInfoQuery = new UserInfoQuery();
        userInfoQuery.setNickName(nickName);
        userInfoQuery.setPageNo(pageNo);
        userInfoQuery.setPageSize(pageSize);
        PaginationResultVO resultVO = this.userInfoService.findListByPage(userInfoQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/updateUserStatus")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO updateUserStatus(@NotNull Integer status, @NotEmpty String userId){
        this.userInfoService.updateUserStatus(status, userId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/forceOffline")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO forceOffline(@NotEmpty String userId){
        this.userInfoService.forceOffline(userId);
        return getSuccessResponseVO(null);
    }
}
