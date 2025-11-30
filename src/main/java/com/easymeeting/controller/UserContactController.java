package com.easymeeting.controller;

import com.easymeeting.annotation.GlobalInterceptor;
import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.entity.enums.UserContactApplyStatusEnum;
import com.easymeeting.entity.enums.UserContactStatusEnum;
import com.easymeeting.entity.po.UserContact;
import com.easymeeting.entity.po.UserContactApply;
import com.easymeeting.entity.po.UserInfo;
import com.easymeeting.entity.query.SimplePage;
import com.easymeeting.entity.query.UserContactApplyQuery;
import com.easymeeting.entity.query.UserContactQuery;
import com.easymeeting.entity.vo.ResponseVO;
import com.easymeeting.entity.vo.UserInfoVO4Search;
import com.easymeeting.service.UserContactApplyService;
import com.easymeeting.service.UserContactService;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/userContact")
@Validated
@Slf4j
public class UserContactController extends ABaseController{

    @Resource
    private UserContactService userContactService;

    @Resource
    private UserContactApplyService userContactApplyService;

    @RequestMapping("/searchContact")
    @GlobalInterceptor
    public ResponseVO searchContact(@NotEmpty String userId){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        UserInfoVO4Search userInfoVO4Search = userContactService.searchContact(tokenUserInfoDto.getUserId(), userId);
        return getSuccessResponseVO(userInfoVO4Search);
    }

    @RequestMapping("/contactApply")
    @GlobalInterceptor
    public ResponseVO contactApply(@NotEmpty String receiveUserId){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();

        UserContactApply userContactApply = new UserContactApply();
        userContactApply.setApplyUserId(tokenUserInfoDto.getUserId());
        userContactApply.setReceiveUserId(receiveUserId);
        Integer status = userContactApplyService.saveUserContactApply(userContactApply);
        return getSuccessResponseVO(status);
    }

    @RequestMapping("/dealWithApply")
    @GlobalInterceptor
    public ResponseVO dealWithApply(@NotEmpty String applyUserId, @NotNull Integer status){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        userContactApplyService.dealWithApply(applyUserId, tokenUserInfoDto.getUserId(), tokenUserInfoDto.getNickName(), status);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadContactApply")
    @GlobalInterceptor
    public ResponseVO loadContactApply(){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();

        UserContactApplyQuery applyQuery = new UserContactApplyQuery();
        applyQuery.setReceiveUserId(tokenUserInfoDto.getUserId());
        applyQuery.setQueryUserInfo(true);
        applyQuery.setOrderBy("last_apply_time");
        applyQuery.setOrderDirection("desc");
        applyQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        List<UserContactApply> applyList = this.userContactApplyService.findListByParam(applyQuery);
        return getSuccessResponseVO(applyList);
    }

    @RequestMapping("/loadContactUser")
    @GlobalInterceptor
    public ResponseVO loadContactUser(){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();

        UserContactQuery contactQuery = new UserContactQuery();
        contactQuery.setUserId(tokenUserInfoDto.getUserId());
        contactQuery.setQueryUserInfo(true);
        contactQuery.setOrderBy("last_update_time");
        contactQuery.setOrderDirection("desc");
        List<UserContact> userContactList = this.userContactService.findListByParam(contactQuery);
        return getSuccessResponseVO(userContactList);
    }

    @RequestMapping("/loadContactApplyDealWithCount")
    @GlobalInterceptor
    public ResponseVO loadContactApplyDealWithCount(){

        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        UserContactApplyQuery applyQuery = new UserContactApplyQuery();
        applyQuery.setReceiveUserId(tokenUserInfoDto.getUserId());
        applyQuery.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
        Integer count = this.userContactApplyService.findCountByParam(applyQuery);
        return getSuccessResponseVO(count);
    }

    @RequestMapping("/delContact")
    @GlobalInterceptor
    public ResponseVO delContact(@NotEmpty String contactId, @NotNull Integer status){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        userContactService.delContact(tokenUserInfoDto.getUserId(), contactId, status);

        return getSuccessResponseVO(null);
    }


}
