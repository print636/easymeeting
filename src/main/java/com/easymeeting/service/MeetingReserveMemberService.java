package com.easymeeting.service;

import com.easymeeting.entity.po.MeetingReserveMember;
import com.easymeeting.entity.query.MeetingReserveMemberQuery;
import com.easymeeting.entity.vo.PaginationResultVO;

import java.util.List;

public interface MeetingReserveMemberService {

    List<MeetingReserveMember> findListByParam(MeetingReserveMemberQuery param);

    Integer findCountByParam(MeetingReserveMemberQuery param);

    PaginationResultVO<MeetingReserveMember> findListByPage(MeetingReserveMemberQuery param);

    Integer add(MeetingReserveMember bean);

    Integer addBatch(List<MeetingReserveMember> listBean);

    Integer addOrUpdateBatch(List<MeetingReserveMember> listBean);

    Integer updateByParam(MeetingReserveMember bean, MeetingReserveMemberQuery param);

    Integer deleteByParam(MeetingReserveMemberQuery param);

    Integer deleteByMeetingIdAndInviteUserId(String meetingId, String inviteUserId);
}

