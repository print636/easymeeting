package com.easymeeting.redis;

import com.easymeeting.entity.constants.Constants;
import com.easymeeting.entity.dto.MeetingMemberDto;
import com.easymeeting.entity.dto.SysSettingDto;
import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.entity.enums.MeetingMemberStatusEnum;
import com.easymeeting.entity.po.MeetingMember;
import com.easymeeting.utils.StringTools;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RedisComponent {

    @Resource
    private RedisUtils redisUtils;

    public String saveCheckCode(String code){
        String checkCodeKey = UUID.randomUUID().toString();
        redisUtils.set_time(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey, code, 60 * 10);
        return checkCodeKey;
    }

    public String getCheckCode(String checkCodeKey){
        return (String) redisUtils.get(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void cleanCheckCode(String checkCodeKey) {
        redisUtils.delete(Constants.REDIS_KEY_CHECK_CODE + checkCodeKey);
    }

    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto){
        redisUtils.set_time(Constants.REDIS_KEY_WS_TOKEN+tokenUserInfoDto.getToken(),tokenUserInfoDto,Constants.REDIS_KEY_EXPIRES_DaY);
        redisUtils.set_time(Constants.REDIS_KBY_WS_TOKEN_USERID+tokenUserInfoDto.getUserId(),tokenUserInfoDto.getToken(),Constants.REDIS_KEY_EXPIRES_DaY);
    }

    public void cleanTokenByUserId(String userId){
        String token = (String) redisUtils.get(Constants.REDIS_KBY_WS_TOKEN_USERID + userId);
        if(!StringTools.isEmpty(token)){
            redisUtils.delete(Constants.REDIS_KEY_WS_TOKEN + token);
        }
        redisUtils.delete(Constants.REDIS_KBY_WS_TOKEN_USERID + userId);
    }

    public TokenUserInfoDto getTokenUserInfoDtoByToken(String token) {
        return (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN + token);
    }

    public TokenUserInfoDto getTokenUserInfoDtoByUserId(String userId) {
        String token = (String)redisUtils.get(Constants.REDIS_KBY_WS_TOKEN_USERID + userId);
        return (TokenUserInfoDto)getTokenUserInfoDtoByToken(token);
    }

    public void add2Meeting(String meetingId, MeetingMemberDto meetingMemberDto){
        redisUtils.hset(Constants.REDIS_KEY_MEETING_RO0M + meetingId, meetingMemberDto.getUserId(), meetingMemberDto);
    }

    public List<MeetingMemberDto> getMeetingMemberList(String meetingId){
        List<MeetingMemberDto> meetingMemberDtoList = redisUtils.hvals(Constants.REDIS_KEY_MEETING_RO0M + meetingId);
        meetingMemberDtoList = meetingMemberDtoList.stream().sorted(Comparator.comparing(MeetingMemberDto::getJoinTime)).collect(Collectors.toList());
        return meetingMemberDtoList;
    }

    public MeetingMemberDto getMeetingMember(String meetingId, String userId){
        return (MeetingMemberDto) redisUtils.hget(Constants.REDIS_KEY_MEETING_RO0M + meetingId, userId);
    }

    public Boolean exitMeeting(String meetingId, String userId, MeetingMemberStatusEnum statusEnum) {
        MeetingMemberDto meetingMemberDto = getMeetingMember(meetingId, userId);
        if(meetingMemberDto == null){
            return false;
        }
        meetingMemberDto.setStatus(statusEnum.getStatus());
        add2Meeting(meetingId, meetingMemberDto);
        return true;
    }

    public void removeAllMeetingMember(String meetingId){
        List<MeetingMemberDto> meetingMemberDtoList = getMeetingMemberList(meetingId);
        List<String> userIdList = meetingMemberDtoList.stream().map(MeetingMemberDto::getUserId).collect(Collectors.toList());
        if(userIdList.isEmpty()){
            return;
        }
        redisUtils.hdel(Constants.REDIS_KEY_MEETING_RO0M + meetingId, userIdList.toArray(new String[userIdList.size()]));
    }

    public void addInviteInfo(String meetingId, String userId){
        redisUtils.set_time(Constants.REDIs_KEY_INVITE_MEMBER + userId + meetingId, meetingId, 60 * 10);
    }

    public String getInviteInfo(String userId, String meetingId){
        return (String)redisUtils.get(Constants.REDIs_KEY_INVITE_MEMBER + userId + meetingId);
    }

    public void saveSysSetting(SysSettingDto sysSettingDto) { redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto); }

    public SysSettingDto getSysSetting() {
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        sysSettingDto = sysSettingDto == null ? new SysSettingDto() : sysSettingDto;
        return sysSettingDto;
    }
}
