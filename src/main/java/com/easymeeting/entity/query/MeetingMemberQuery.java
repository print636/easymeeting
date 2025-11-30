package com.easymeeting.entity.query;

import java.util.Date;

public class MeetingMemberQuery {
    private Integer pageNo = 1;
    private Integer pageSize = 10;

    private String meetingId;
    private String userId;
    private String nickName;
    private Integer status;
    private Integer memberType;
    private Integer meetingStatus;
    private Date lastJoinTimeStart;
    private Date lastJoinTimeEnd;

    private String orderBy;
    private String orderDirection = "desc";
    
    private SimplePage simplePage;

    public SimplePage getSimplePage() {
        return simplePage;
    }

    public void setSimplePage(SimplePage simplePage) {
        this.simplePage = simplePage;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public Integer getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(Integer meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    public Date getLastJoinTimeStart() {
        return lastJoinTimeStart;
    }

    public void setLastJoinTimeStart(Date lastJoinTimeStart) {
        this.lastJoinTimeStart = lastJoinTimeStart;
    }

    public Date getLastJoinTimeEnd() {
        return lastJoinTimeEnd;
    }

    public void setLastJoinTimeEnd(Date lastJoinTimeEnd) {
        this.lastJoinTimeEnd = lastJoinTimeEnd;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}


