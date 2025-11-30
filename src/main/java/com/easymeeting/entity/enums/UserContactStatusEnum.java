package com.easymeeting.entity.enums;

import com.easymeeting.entity.po.UserContactApply;

public enum UserContactStatusEnum {

    FRIEND(1, "好友"),
    DEL(2, "已删除好友"),
    BLACKLIST(3,"已拉黑好友");

    private Integer status;
    private String desc;

    UserContactStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static UserContactStatusEnum getByStatus(Integer status) {
        for(UserContactStatusEnum item : UserContactStatusEnum.values()){
            if(item.getStatus().equals(status)){
                return item;
            }
        }
        return null;
    }

    public Integer getStatus(){
        return status;
    }

    public String getDesc(){
        return desc;
    }
}
