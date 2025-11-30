package com.easymeeting.entity.enums;

import com.easymeeting.entity.query.MeetingMemberQuery;

import java.util.zip.Inflater;

public enum MeetingReserveStatusEnum {
    NO_START(0, "未开始"),
    FINISHED(1, "已结束");

    private Integer status;
    private String desc;

    MeetingReserveStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static MeetingReserveStatusEnum getByStatus(Integer status) {
        for(MeetingReserveStatusEnum item : MeetingReserveStatusEnum.values()) {
            if(item.getStatus().equals(status)){
                return item;
            }
        }
        return null;
    }

    public Integer getStatus() { return status; }

    public String getDesc() { return desc; }
}
