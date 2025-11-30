package com.easymeeting.entity.enums;

public enum MessageStatusEnum {

    SENDING(0,"正在发送"),
    SENDED(1,"已发送");

    private Integer status;
    private String desc;

    MessageStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static MessageStatusEnum getByStatus(Integer status) {
        for(MessageStatusEnum messageStatusEnum : MessageStatusEnum.values()) {
            if(messageStatusEnum.getStatus().equals(status)) {
                return messageStatusEnum;
            }
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc(){
        return desc;
    }
}
