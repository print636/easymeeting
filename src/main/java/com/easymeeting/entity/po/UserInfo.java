package com.easymeeting.entity.po;

import com.easymeeting.entity.constants.Constants;

import java.util.Date;

public class UserInfo {
    private String userId;
    private String email;
    private String nickName;
    private Integer sex;
    private String password;
    private Integer status;
    private Date createTime;
    private Long lastLoginTime;
    private Long lastOffTime;
    private String meetingNo;

    private Integer onLineType;

    public Integer getOnLineType() {
        if (lastLoginTime != null && lastLoginTime > lastOffTime) {
            return Constants.ONE;
        } else {
            return Constants.ZERO;
        }
    }

    public void setOnLineType(Integer onLineType) {
        this.onLineType = onLineType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getLastOffTime() {
        return lastOffTime;
    }

    public void setLastOffTime(Long lastOffTime) {
        this.lastOffTime = lastOffTime;
    }

    public String getMeetingNo() {
        return meetingNo;
    }

    public void setMeetingNo(String meetingNo) {
        this.meetingNo = meetingNo;
    }
}


