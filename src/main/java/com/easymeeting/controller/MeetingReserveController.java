package com.easymeeting.controller;

import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.entity.enums.MeetingReserveStatusEnum;
import com.easymeeting.entity.po.MeetingReserve;
import com.easymeeting.entity.query.MeetingReserveQuery;
import com.easymeeting.entity.vo.ResponseVO;
import com.easymeeting.service.MeetingReserveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@RestController
@RequestMapping("/meetingReserve")
@Validated
@Slf4j
public class MeetingReserveController extends ABaseController{

    @Resource
    private MeetingReserveService meetingReserveService;


    @RequestMapping("/loadTodayMeeting")
    public ResponseVO loadTodayMeeting(){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        MeetingReserveQuery query = new MeetingReserveQuery();
        query.setUserId(tokenUserInfoDto.getUserId());
        Date curDate = new Date();
        query.setStartTimeStart(curDate);
        query.setStartTimeEnd(curDate);
        query.setOrderBy("start_time");
        query.setOrderDirection("asc");
        query.setStatus(MeetingReserveStatusEnum.NO_START.getStatus());
        query.setQueryUserInfo(true);
        return getSuccessResponseVO(meetingReserveService.findListByParam(query));
    }

    @RequestMapping("/loadMeetingReserve")
    public ResponseVO loadMeetingReserve() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        MeetingReserveQuery query = new MeetingReserveQuery();
        query.setUserId(tokenUserInfoDto.getUserId());
        query.setOrderBy("start_time");
        query.setOrderDirection("desc");
        query.setStatus(MeetingReserveStatusEnum.NO_START.getStatus());
        //查创建者（谁预约的会议）
        query.setQueryUserInfo(true);
        return getSuccessResponseVO(meetingReserveService.findListByPage(query));
    }


    @RequestMapping("/createMeetingReserve")
    public ResponseVO createMeetingReserve(MeetingReserve meetingReserve){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        meetingReserve.setCreateUserId(tokenUserInfoDto.getUserId());
        meetingReserveService.createMeetingReserve(meetingReserve);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delMeetingReserve")
    public ResponseVO delMeetingReserve(@NotEmpty String meetingId){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        meetingReserveService.deleteMeetingReserve(meetingId, tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delMeetingReserveByUser")
    public ResponseVO delMeetingReserveByUser(@NotEmpty String meetingId){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        meetingReserveService.deleteMeetingReserveByUser(meetingId, tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }

}
