package com.easymeeting.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingExitDto implements Serializable {
    public String exitUserId;

    private List<MeetingMemberDto> meetingMemberDtoList;
    private Integer exitStatus;

    public String getExitUserId() {
        return exitUserId;
    }

    public void setExitUserId(String exitUserId) {
        this.exitUserId = exitUserId;
    }

    public List<MeetingMemberDto> getMeetingMemberDtoList() {
        return meetingMemberDtoList;
    }

    public void setMeetingMemberDtoList(List<MeetingMemberDto> meetingMemberDtoList) {
        this.meetingMemberDtoList = meetingMemberDtoList;
    }

    public Integer getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(Integer exitStatus) {
        this.exitStatus = exitStatus;
    }




}
