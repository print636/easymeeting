package com.easymeeting.controller;
import com.easymeeting.annotation.GlobalInterceptor;
import com.easymeeting.controller.ABaseController;
import com.easymeeting.entity.query.MeetingInfoQuery;
import com.easymeeting.entity.query.MeetingMemberQuery;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.entity.vo.ResponseVO;
import com.easymeeting.redis.RedisComponent;
import com.easymeeting.service.MeetingInfoService;
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
public class AdminMeetingController extends ABaseController {
    @Resource
    private MeetingInfoService meetingInfoService;

    @RequestMapping("/loadAdminMeeting")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO loadAdminMeeting(String meetingName, Integer pageNo, Integer pageSize){
        MeetingInfoQuery infoQuery = new MeetingInfoQuery();
        infoQuery.setMeetingName(meetingName);
        infoQuery.setPageNo(pageNo);
        infoQuery.setPageSize(pageSize);
        infoQuery.setOrderBy("create_time");
        infoQuery.setOrderDirection("desc");
        infoQuery.setQueryMemberCount(true);
        PaginationResultVO resultVO = this.meetingInfoService.findListByPage(infoQuery);

        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/adminFinishMeeting")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO adminFinishMeeting(@NotEmpty String meetingId){
        this.meetingInfoService.finishMeeting(meetingId, null);
        return getSuccessResponseVO(null);
    }
}