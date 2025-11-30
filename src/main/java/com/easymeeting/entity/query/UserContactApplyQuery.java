package com.easymeeting.entity.query;

import java.util.Date;

public class UserContactApplyQuery {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private Integer applyId;
    private String applyUserId;
    private String receiveUserId;
    private Date lastApplyTimeStart;
    private Date lastApplyTimeEnd;
    private Integer status;

    private String orderBy;
    private String orderDirection = "desc";
    
    private SimplePage simplePage;

    private Boolean queryUserInfo;

    public Boolean getQueryUserInfo() {
        return queryUserInfo;
    }

    public void setQueryUserInfo(Boolean queryUserInfo) {
        this.queryUserInfo = queryUserInfo;
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

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public Date getLastApplyTimeStart() {
        return lastApplyTimeStart;
    }

    public void setLastApplyTimeStart(Date lastApplyTimeStart) {
        this.lastApplyTimeStart = lastApplyTimeStart;
    }

    public Date getLastApplyTimeEnd() {
        return lastApplyTimeEnd;
    }

    public void setLastApplyTimeEnd(Date lastApplyTimeEnd) {
        this.lastApplyTimeEnd = lastApplyTimeEnd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public SimplePage getSimplePage() {
        return simplePage;
    }

    public void setSimplePage(SimplePage simplePage) {
        this.simplePage = simplePage;
    }
}

