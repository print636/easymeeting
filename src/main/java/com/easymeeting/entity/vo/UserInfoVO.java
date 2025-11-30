package com.easymeeting.entity.vo;

import java.io.Serializable;

public class UserInfoVO implements Serializable {
    private String userId;
    private String nickName;
    private Integer sex;
    private String token;
    private String meetingNo;
    private Boolean admin;

    /**
     * 获取
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取
     * @return nickName
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置
     * @param nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 获取
     * @return sex
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置
     * @param sex
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取
     * @return meetingNo
     */
    public String getMeetingNo() {
        return meetingNo;
    }

    /**
     * 设置
     * @param meetingNo
     */
    public void setMeetingNo(String meetingNo) {
        this.meetingNo = meetingNo;
    }

    /**
     * 获取
     * @return admin
     */
    public Boolean getAdmin() {
        return admin;
    }

    /**
     * 设置
     * @param admin
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }


}
