package com.easymeeting.service;

import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.entity.enums.MeetingMemberStatusEnum;
import com.easymeeting.entity.po.MeetingInfo;
import com.easymeeting.entity.po.MeetingMember;
import com.easymeeting.entity.query.MeetingInfoQuery;
import com.easymeeting.entity.vo.PaginationResultVO;

import java.util.List;

public interface MeetingInfoService {

    List<MeetingInfo> findListByParam(MeetingInfoQuery param);

    Integer findCountByParam(MeetingInfoQuery param);

    PaginationResultVO<MeetingInfo> findListByPage(MeetingInfoQuery param);

    Integer add(MeetingInfo bean);

    Integer addBatch(List<MeetingInfo> listBean);

    Integer addOrUpdateBatch(List<MeetingInfo> listBean);

    Integer updateByParam(MeetingInfo bean, MeetingInfoQuery param);

    Integer deleteByParam(MeetingInfoQuery param);

    MeetingInfo getMeetingInfoByMeetingId(String meetingId);

    Integer updateMeetingInfoByMeetingId(MeetingInfo bean,String meetingId);

    Integer deleteMeetingInfoByMeetingId(String meetingId);

    void quickMeeting(MeetingInfo meetingInfo,String nickName);

    void joinMeeting(String meetingId, String userId, String nickname, Integer sex, Boolean videoOpen);

    String preJoinMeeting(String meetingNo, TokenUserInfoDto tokenUserInfoDto, String password);

    void exitMeeting(TokenUserInfoDto tokenUserInfoDto, MeetingMemberStatusEnum statusEnum);

    void forceExitMeeting(TokenUserInfoDto tokenUserInfoDto, String userId,MeetingMemberStatusEnum statusEnum);

    void finishMeeting(String MeetingId, String userId);

    void reserveJoinMeeting(String meetingId, TokenUserInfoDto tokenUserInfoDto, String JoinPassword);

    void inviteMember(TokenUserInfoDto tokenUserInfoDto, String selectContactIds);

    public void acceptInvite(TokenUserInfoDto tokenUserInfoDto, String meetingId);

    void updateMemberOpenVideo(String currentMeetingId, String userId, Boolean openVideo);
}
