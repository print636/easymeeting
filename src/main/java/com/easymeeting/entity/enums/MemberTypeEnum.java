package com.easymeeting.entity.enums;

import com.easymeeting.entity.vo.PaginationResultVO;

public enum MemberTypeEnum {
    NORMAL(0,"普通成员"),
    COMPERE(1,"主持人");

    private Integer type;
    private String desc;

    MemberTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static MemberTypeEnum getByType(Integer type){
        for(MemberTypeEnum item : MemberTypeEnum.values()){
            if(item.getType().equals(type)){
                return item;
            }
        }
        return null;
    }

    public Integer getType() {return type;}

    public String getDesc(){ return desc;}
}
