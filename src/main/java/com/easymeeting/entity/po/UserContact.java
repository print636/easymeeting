package com.easymeeting.entity.po;

import java.io.Serializable;
import java.util.Date;

public class UserContact implements Serializable {
    private String userId;
    private String contactId;
    private Integer status;
    private Date lastUpdateTime;

    private String nickName;

    private Date lastLoginTime;

    private Date lastOffTime;

    public Integer getGetOnlineType() {
        return getOnlineType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastOffTime() {
        return lastOffTime;
    }

    public void setLastOffTime(Date lastOffTime) {
        this.lastOffTime = lastOffTime;
    }

    private Integer getOnlineType;

    public Integer getOnlineType(){
        if(lastLoginTime != null && lastLoginTime.after(lastOffTime)){
            return 1;
        }else{
            return 0;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}



