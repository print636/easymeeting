package com.easymeeting.service.impl;

import com.easymeeting.entity.dto.*;
import com.easymeeting.entity.enums.*;
import com.easymeeting.entity.po.*;
import com.easymeeting.entity.query.*;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.exception.BusinessException;
import com.easymeeting.mappers.*;
import com.easymeeting.redis.RedisComponent;
import com.easymeeting.service.MeetingInfoService;
import com.easymeeting.service.MeetingMemberService;
import com.easymeeting.utils.JsonUtils;
import com.easymeeting.utils.StringTools;
import com.easymeeting.websocket.ChannelContextUtils;
import com.easymeeting.websocket.message.MessageHandler;
import lombok.experimental.PackagePrivate;
import org.aspectj.bridge.Message;
import org.springframework.stereotype.Service;
import com.easymeeting.utils.ArrayUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("meetingInfoService")
public class MeetingInfoServiceImpl implements MeetingInfoService {

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private ChannelContextUtils channelContextUtils;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private MeetingMemberMapper<MeetingMember, MeetingMemberQuery> meetingMemberMapper;

    @Resource
    private MeetingInfoMapper<MeetingInfo, MeetingInfoQuery> meetingInfoMapper;

    @Resource
    private MeetingReserveMapper<MeetingReserve, MeetingReserveQuery> meetingReserveMapper;

    @Resource
    private MeetingReserveMemberMapper<MeetingReserveMember, MeetingReserveMemberQuery> meetingReserveMemberMapper;

    @Resource
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    @Override
    public List<MeetingInfo> findListByParam(MeetingInfoQuery param) {
        return this.meetingInfoMapper.selectListByParam(param);
    }

/**
 * 根据查询参数获取会议信息数量
 * 该方法重写了父类的接口实现，用于查询符合特定条件的会议记录总数
 *
 * @param param 查询参数对象，包含查询条件的封装
 * @return 返回符合条件的会议记录总数，类型为Integer
 */
    @Override
    public Integer findCountByParam(MeetingInfoQuery param) {
        return this.meetingInfoMapper.selectCountByParam(param);
    }

    @Override
    public PaginationResultVO<MeetingInfo> findListByPage(MeetingInfoQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? 15 : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNo(),count,pageSize);
        param.setSimplePage(page);
        List<MeetingInfo> list = this.findListByParam(param);
        PaginationResultVO<MeetingInfo> vo = new PaginationResultVO<MeetingInfo>(count,page.getPageSize(),page.getPageNo(),page.getPageTotal(),list);
        return vo;
    }

    @Override
    public Integer add(MeetingInfo bean) {
        return this.meetingInfoMapper.insert(bean);
    }

    @Override
    public Integer addBatch(List<MeetingInfo> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        return this.meetingInfoMapper.insertBatch(listBean);
    }

    @Override
    public Integer addOrUpdateBatch(List<MeetingInfo> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        return this.meetingInfoMapper.insertOrUpdateBatch(listBean);
    }

    @Override
    public Integer updateByParam(MeetingInfo bean, MeetingInfoQuery param) {
        return this.meetingInfoMapper.updateByParam(bean, param);
    }

    @Override
    public Integer deleteByParam(MeetingInfoQuery param) {
        return this.meetingInfoMapper.deleteByParam(param);
    }

    @Override
    public MeetingInfo getMeetingInfoByMeetingId(String meetingId) {
        return this.meetingInfoMapper.selectByMeetingId(meetingId);
    }

    @Override
    public Integer updateMeetingInfoByMeetingId(MeetingInfo bean, String meetingId) {
        return this.meetingInfoMapper.updateByMeetingId(bean, meetingId);
    }

    @Override
    public Integer deleteMeetingInfoByMeetingId(String meetingId) {
        return this.meetingInfoMapper.deleteByMeetingId(meetingId);
    }

    @Override
    public void quickMeeting(MeetingInfo meetingInfo, String nickName) {
        Date curDate = new Date();
        meetingInfo.setCreateTime(curDate);
        meetingInfo.setMeetingId(StringTools.getMeetingNoOrMeetingId());
        meetingInfo.setStartTime(curDate);
        meetingInfo.setStatus(MeetingStatusEnum.RUNNING.getStatus());
        meetingInfoMapper.insert(meetingInfo);
    }


    private void addMeetingMember(String meetingId, String userId, String nickName, Integer memberType){
        MeetingMember meetingMember = new MeetingMember();
        meetingMember.setMeetingId(meetingId);
        meetingMember.setUserId(userId);
        meetingMember.setNickName(nickName);
        meetingMember.setLastJoinTime(new Date());
        meetingMember.setStatus(MeetingMemberStatusEnum.NORMAL.getStatus());
        meetingMember.setMemberType(memberType);
        meetingMember.setMeetingStatus(MeetingStatusEnum.RUNNING.getStatus());
        meetingMemberMapper.insertOrUpdate(meetingMember);

    }

    private void add2Meeting(String meetingId, String userId, String nickName, Integer sex, Integer memberType, Boolean videoOpen){
        MeetingMemberDto meetingMemberDto = new MeetingMemberDto();
        meetingMemberDto.setUserId(userId);
        meetingMemberDto.setNickName(nickName);
        meetingMemberDto.setJoinTime(System.currentTimeMillis());
        meetingMemberDto.setMemberType(memberType);
        meetingMemberDto.setStatus(MeetingMemberStatusEnum.NORMAL.getStatus());
        meetingMemberDto.setOpenVideo(videoOpen);
        meetingMemberDto.setSex(sex);
        redisComponent.add2Meeting(meetingId,meetingMemberDto);
    }

    private void checkMeetingJoin(String meetingId, String userId) {
        MeetingMemberDto meetingMemberDto = redisComponent.getMeetingMember(meetingId, userId);
        if(meetingMemberDto != null && MeetingMemberStatusEnum.BLACKLIST.getStatus().equals(meetingMemberDto.getStatus())){
            throw new BusinessException("您已被拉黑，无法加入会议");
        }

    }

    @Override
    public void joinMeeting(String meetingId, String userId, String nickName, Integer sex, Boolean videoOpen) {
        if(StringTools.isEmpty(meetingId)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        MeetingInfo meetingInfo = this.meetingInfoMapper.selectByMeetingId(meetingId);
        if(meetingInfo == null || MeetingStatusEnum.FINISHED.getStatus().equals(meetingInfo.getStatus())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        //校验用户
        this.checkMeetingJoin(meetingId, userId);
        //加入成员
        MemberTypeEnum memberTypeEnum = meetingInfo.getCreateUserId().equals(userId) ? MemberTypeEnum.COMPERE : MemberTypeEnum.NORMAL;
        this.addMeetingMember(meetingId, userId, nickName, memberTypeEnum.getType());
        //加入会议
        this.add2Meeting(meetingId, userId, nickName, sex, memberTypeEnum.getType(), videoOpen);
        //加入ws 房间
        channelContextUtils.addMeetingRoom(meetingId,userId);

        //发送ws 消息

        MeetingJoinDto meetingJoinDto = new MeetingJoinDto();
        meetingJoinDto.setNewMember(redisComponent.getMeetingMember(meetingId, userId));
        meetingJoinDto.setMeetingMemberList(redisComponent.getMeetingMemberList(meetingId));

        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMeetingId(meetingId);
        messageSendDto.setMessageType(MessageTypeEnum.ADD_MEETING_ROOM.getType());
        messageSendDto.setMessageContent(meetingJoinDto);
        messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.GROUP.getType());

        messageHandler.sendMessage(messageSendDto);
    }

    @Override
    public String preJoinMeeting(String meetingNo, TokenUserInfoDto tokenUserInfoDto, String password) {
        String userId = tokenUserInfoDto.getUserId();
        MeetingInfoQuery meetingInfoQuery = new MeetingInfoQuery();
        meetingInfoQuery.setMeetingNo(meetingNo);
        meetingInfoQuery.setStatus(MeetingStatusEnum.RUNNING.getStatus());
        List<MeetingInfo> meetingInfoList = meetingInfoMapper.selectListByParam(meetingInfoQuery);
        if(meetingInfoList.isEmpty()){
            throw new BusinessException("会议不存在");
        }
        MeetingInfo meetingInfo = meetingInfoList.get(0);

        if(!MeetingStatusEnum.RUNNING.getStatus().equals(meetingInfo.getStatus())){
            throw new BusinessException("你有未结束的会议，无法加入其他会议");
        }

        checkMeetingJoin(meetingInfo.getMeetingId(), userId);

        if(MeetingJoinTypeEnum.PASSWORD.getType().equals(meetingInfo.getJoinType()) && !meetingInfo.getJoinPassword().equals(password)){
            throw new BusinessException("密码错误");
        }

        tokenUserInfoDto.setCurrentMeetingId(meetingInfo.getMeetingId());
        redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);

        return meetingInfo.getMeetingId();

    }

    @Override
    public void exitMeeting(TokenUserInfoDto tokenUserInfoDto, MeetingMemberStatusEnum statusEnum) {
        String meetingId = tokenUserInfoDto.getCurrentMeetingId();
        if(StringTools.isEmpty(meetingId)){
            return;
        }
        String userId = tokenUserInfoDto.getUserId();

        Boolean exit = redisComponent.exitMeeting(meetingId, userId, statusEnum);
        if(!exit){
            tokenUserInfoDto.setCurrentMeetingId(null);
            redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);
            return;
        }
        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMessageType(MessageTypeEnum.EXIT_MEETING_ROOM.getType());
        //清空当前正在进行的会议
        tokenUserInfoDto.setCurrentMeetingId(null);
        redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);

        List<MeetingMemberDto> meetingMemberDtoList = redisComponent.getMeetingMemberList(meetingId);

        MeetingExitDto exitDto = new MeetingExitDto();
        exitDto.setMeetingMemberDtoList(meetingMemberDtoList);
        exitDto.setExitUserId(userId);
        exitDto.setExitStatus(statusEnum.getStatus());

        messageSendDto.setMessageContent(JsonUtils.convertObj2Json(exitDto));
        messageSendDto.setMeetingId(meetingId);
        messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.GROUP.getType());
        messageHandler.sendMessage(messageSendDto);

        List<MeetingMemberDto> onLineMemberList =
        meetingMemberDtoList.stream().filter(meetingMemberDto -> MeetingMemberStatusEnum.NORMAL.getStatus().equals(meetingMemberDto.getStatus())).collect(Collectors.toList());

        if(onLineMemberList.isEmpty()){
            MeetingReserve meetingReserve = this.meetingReserveMapper.selectByMeetingId(meetingId);
            if(meetingReserve == null){
                finishMeeting(meetingId, null);
                return;
            }
            if(System.currentTimeMillis() > meetingReserve.getStartTime().getTime() + meetingReserve.getDuration()*60*1000) {
                finishMeeting(meetingId, null);
                return;
            }
        }
        //如果是被踢出或者拉黑的，则更新状态
        if(ArrayUtils.contains(new Integer[]{MeetingMemberStatusEnum.KICK_OUT.getStatus(),MeetingMemberStatusEnum.BLACKLIST.getStatus()},statusEnum.getStatus())){
            MeetingMember meetingMember = new MeetingMember();
            meetingMember.setStatus(statusEnum.getStatus());
            MeetingMemberQuery meetingMemberQuery = new MeetingMemberQuery();
            meetingMemberQuery.setMeetingId(meetingId);
            meetingMemberQuery.setUserId(userId);
            meetingMemberMapper.updateByParam(meetingMember,meetingMemberQuery);
        }

    }

    //涉及多个数据库加上事务注解
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forceExitMeeting(TokenUserInfoDto tokenUserInfoDto, String userId,MeetingMemberStatusEnum statusEnum) {
        MeetingInfo meetingInfo = this.meetingInfoMapper.selectByMeetingId(tokenUserInfoDto.getCurrentMeetingId());
        //判断是否是创建者
        if(!meetingInfo.getCreateUserId().equals(tokenUserInfoDto.getUserId())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        TokenUserInfoDto userInfoDto = this.redisComponent.getTokenUserInfoDtoByUserId(userId);
        exitMeeting(userInfoDto, statusEnum);
    }

    @Override
    public void finishMeeting(String meetingId, String userId) {
        MeetingInfo meetingInfo = this.meetingInfoMapper.selectByMeetingId(meetingId);
        if(userId != null && !meetingInfo.getCreateUserId().equals(userId)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        MeetingInfo updateInfo = new MeetingInfo();
        updateInfo.setStatus(MeetingStatusEnum.FINISHED.getStatus());
        updateInfo.setEndTime(new Date());
        meetingInfoMapper.updateByMeetingId(updateInfo,meetingId);

        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.GROUP.getType());
        messageSendDto.setMessageType(MessageTypeEnum.FINIS_MEETING.getType());
        messageSendDto.setMeetingId(meetingId);
        messageHandler.sendMessage(messageSendDto);

        MeetingMember meetingMember = new MeetingMember();
        meetingMember.setMeetingStatus(MeetingStatusEnum.FINISHED.getStatus());
        MeetingMemberQuery meetingMemberQuery = new MeetingMemberQuery();
        meetingMemberQuery.setMeetingId(meetingId);
        meetingMemberMapper.updateByParam(meetingMember, meetingMemberQuery);

        //更新预约会议状态
        MeetingReserve updateMeetingReserve = new MeetingReserve();
        updateMeetingReserve.setStatus(MeetingReserveStatusEnum.FINISHED.getStatus());
        updateMeetingReserve.setMeetingId(meetingId);
        meetingReserveMapper.updateByMeetingId(updateMeetingReserve, meetingId);


        List<MeetingMemberDto> meetingMemberDtoList = redisComponent.getMeetingMemberList(meetingId);
        for(MeetingMemberDto meetingMemberDto:meetingMemberDtoList){
            TokenUserInfoDto userInfoDto = this.redisComponent.getTokenUserInfoDtoByUserId(meetingMemberDto.getUserId());
            userInfoDto.setCurrentMeetingId(null);
            redisComponent.saveTokenUserInfoDto(userInfoDto);
        }

        redisComponent.removeAllMeetingMember(meetingId);

    }

    @Override
    public void reserveJoinMeeting(String meetingId, TokenUserInfoDto tokenUserInfoDto, String JoinPassword) {
        String userId = tokenUserInfoDto.getUserId();
        if (!StringTools.isEmpty(tokenUserInfoDto.getCurrentMeetingId()) && !meetingId.equals(tokenUserInfoDto.getCurrentMeetingId())) {
            throw new BusinessException("你有未结束的会议无法加入其他会议");
        }
        checkMeetingJoin(meetingId, userId);
        MeetingReserve meetingReserve = this.meetingReserveMapper.selectByMeetingId(meetingId);
        if (meetingReserve == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        MeetingReserveMember member = this.meetingReserveMemberMapper.selectByMeetingIdAndInviteUserId(meetingId, tokenUserInfoDto.getUserId());
        if (member == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if (MeetingJoinTypeEnum.PASSWORD.getType().equals(meetingReserve.getJoinType()) && !meetingReserve.getJoinPassword().equals(JoinPassword)) {
            throw new BusinessException("入会密码不正确");
        }
        MeetingInfo meetingInfo = this.meetingInfoMapper.selectByMeetingId(meetingId);
        //没有会议就创建会议
        if(meetingInfo == null){
            meetingInfo = new MeetingInfo();
            meetingInfo.setMeetingName(meetingReserve.getMeetingName());
            meetingInfo.setMeetingNo(StringTools.getMeetingNoOrMeetingId());
            meetingInfo.setJoinType(meetingReserve.getJoinType());
            meetingInfo.setJoinPassword(meetingReserve.getJoinPassword());
            meetingInfo.setCreateUserId(meetingReserve.getCreateUserId());
            Date curDate = new Date();
            meetingInfo.setCreateTime(curDate);
            meetingInfo.setMeetingId(meetingId);
            meetingInfo.setStartTime(curDate);
            meetingInfo.setStatus(MeetingStatusEnum.RUNNING.getStatus());
            this.meetingInfoMapper.insert(meetingInfo);
        }
        tokenUserInfoDto.setCurrentMeetingId(meetingId);
        redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);

    }

    @Override
    public void inviteMember(TokenUserInfoDto tokenUserInfoDto, String selectContactIds) {
        String[] contactIds = selectContactIds.split(",");
        UserContactQuery contactQuery = new UserContactQuery();
        contactQuery.setUserId(tokenUserInfoDto.getUserId());
        contactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        List<UserContact> contactList = userContactMapper.selectListByParam(contactQuery);
        List<String> contactIdList = contactList.stream().map(UserContact -> UserContact.getContactId()).collect(Collectors.toList());

        if(!contactIdList.containsAll(Arrays.asList(contactIds))){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        MeetingInfo meetingInfo = meetingInfoMapper.selectByMeetingId(tokenUserInfoDto.getCurrentMeetingId());

        for(String contactId : contactIds){
            MeetingMemberDto meetingMemberDto = redisComponent.getMeetingMember(tokenUserInfoDto.getCurrentMeetingId(), contactId);
            //已经在会议中的跳过
            if(meetingMemberDto != null && MeetingMemberStatusEnum.NORMAL.getStatus().equals(meetingMemberDto.getStatus())){
                continue;
            }
            redisComponent.addInviteInfo(tokenUserInfoDto.getCurrentMeetingId(), contactId);

            MessageSendDto messageSendDto = new MessageSendDto();
            messageSendDto.setMessageType(MessageTypeEnum.INVITE_MEMBER_MEETING.getType());
            messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.USER.getType());
            messageSendDto.setReceiveUserId(contactId);

            MeetingInviteDto meetingInviteDto = new MeetingInviteDto();
            meetingInviteDto.setMeetingName(meetingInfo.getMeetingName());
            meetingInviteDto.setMeetingId(tokenUserInfoDto.getCurrentMeetingId());
            meetingInviteDto.setInviteUserName(tokenUserInfoDto.getNickName());
            messageSendDto.setMessageContent(JsonUtils.convertObj2Json(meetingInviteDto));
            messageHandler.sendMessage(messageSendDto);
        }
    }

    @Override
    public void acceptInvite(TokenUserInfoDto tokenUserInfoDto, String meetingId) {
        String redisMeetingId = redisComponent.getInviteInfo(tokenUserInfoDto.getUserId(), meetingId);
        if(null == redisMeetingId){
            throw new BusinessException("邀请信息已过期");
        }
        tokenUserInfoDto.setCurrentMeetingId(meetingId);
        tokenUserInfoDto.setCurrentNickName(tokenUserInfoDto.getNickName());
        redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);
    }

    @Override
    public void updateMemberOpenVideo(String meetingId, String userId, Boolean openVideo) {
        MeetingMemberDto meetingMemberDto = redisComponent.getMeetingMember(meetingId, userId);
        meetingMemberDto.setOpenVideo(openVideo);
        this.redisComponent.add2Meeting(meetingId, meetingMemberDto);

        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMessageType(MessageTypeEnum.METING_USER_VIDEO_CHANGE.getType());
        messageSendDto.setSendUserId(userId);
        messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.GROUP.getType());
        messageSendDto.setMeetingId(meetingId);
        messageHandler.sendMessage(messageSendDto);

    }
}

