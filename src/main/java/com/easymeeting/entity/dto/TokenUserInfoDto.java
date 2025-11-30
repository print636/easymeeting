package com.easymeeting.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenUserInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private String userId;

    private String nickName;
    private Integer sex;
    private String currentMeetingId;
    private String currentNickName;

    private String myMeetingNo;

    private Boolean admin;




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
     * @return currentMeetingId
     */
    public String getCurrentMeetingId() {
        return currentMeetingId;
    }

    /**
     * 设置
     * @param currentMeetingId
     */
    public void setCurrentMeetingId(String currentMeetingId) {
        this.currentMeetingId = currentMeetingId;
    }

    /**
     * 获取
     * @return currentNickName
     */
    public String getCurrentNickName() {
        return currentNickName;
    }

    /**
     * 设置
     * @param currentNickName
     */
    public void setCurrentNickName(String currentNickName) {
        this.currentNickName = currentNickName;
    }

    /**
     * 获取
     * @return myMeetingNo
     */
    public String getMyMeetingNo() {
        return myMeetingNo;
    }

    /**
     * 设置
     * @param myMeetingNo
     */
    public void setMyMeetingNo(String myMeetingNo) {
        this.myMeetingNo = myMeetingNo;
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
