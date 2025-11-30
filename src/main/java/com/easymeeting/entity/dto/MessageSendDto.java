package com.easymeeting.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageSendDto<T> implements Serializable {
    private static final long serialVersionUID = 2L;

    private Integer messageSend2Type;
    private String meetingId;
    //消息类型
    private Integer messageType;
    private String sendUserId;
    private String sendUserNickName;
    private T messageContent;
    private String receiveUserId;
    private Long sendTime;
    private Long messageId;
    private Integer status;
    private String fileName;
    private Integer fileType;
    private Long fileSize;


    /**
     * 获取
     * @return messageSend2Type
     */
    public Integer getMessageSend2Type() {
        return messageSend2Type;
    }

    /**
     * 设置
     * @param messageSend2Type
     */
    public void setMessageSend2Type(Integer messageSend2Type) {
        this.messageSend2Type = messageSend2Type;
    }

    /**
     * 获取
     * @return meetingId
     */
    public String getMeetingId() {
        return meetingId;
    }

    /**
     * 设置
     * @param meetingId
     */
    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    /**
     * 获取
     * @return messageType
     */
    public Integer getMessageType() {
        return messageType;
    }

    /**
     * 设置
     * @param messageType
     */
    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    /**
     * 获取
     * @return sendUserId
     */
    public String getSendUserId() {
        return sendUserId;
    }

    /**
     * 设置
     * @param sendUserId
     */
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     * 获取
     * @return sendUserNickName
     */
    public String getSendUserNickName() {
        return sendUserNickName;
    }

    /**
     * 设置
     * @param sendUserNickName
     */
    public void setSendUserNickName(String sendUserNickName) {
        this.sendUserNickName = sendUserNickName;
    }

    /**
     * 获取
     * @return messageContent
     */
    public T getMessageContent() {
        return messageContent;
    }

    /**
     * 设置
     * @param messageContent
     */
    public void setMessageContent(T messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * 获取
     * @return receiveUserId
     */
    public String getReceiveUserId() {
        return receiveUserId;
    }

    /**
     * 设置
     * @param receiveUserId
     */
    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    /**
     * 获取
     * @return sendTime
     */
    public Long getSendTime() {
        return sendTime;
    }

    /**
     * 设置
     * @param sendTime
     */
    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取
     * @return messageId
     */
    public Long getMessageId() {
        return messageId;
    }

    /**
     * 设置
     * @param messageId
     */
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取
     * @return fileType
     */
    public Integer getFileType() {
        return fileType;
    }

    /**
     * 设置
     * @param fileType
     */
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    /**
     * 获取
     * @return fileSize
     */
    public Long getFileSize() {
        return fileSize;
    }

    /**
     * 设置
     * @param fileSize
     */
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }


}
