package com.easymeeting.entity.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingJoinDto implements Serializable {
    public MeetingMemberDto newMember;

    private List<MeetingMemberDto> meetingMemberList;

    public MeetingMemberDto getNewMember(){ return newMember; }

    public void setNewMember(MeetingMemberDto newMember) { this.newMember = newMember; }

    public List<MeetingMemberDto> getMeetingMemberList() { return meetingMemberList; }

    public void setMeetingMemberList(List<MeetingMemberDto> meetingMemberList) { this.meetingMemberList = meetingMemberList; }
}
