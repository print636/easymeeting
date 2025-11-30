package com.easymeeting.service.impl;

import com.easymeeting.config.AppConfig;
import com.easymeeting.entity.constants.Constants;
import com.easymeeting.entity.dto.MessageSendDto;
import com.easymeeting.entity.enums.*;
import com.easymeeting.entity.po.MeetingChatMessage;
import com.easymeeting.entity.query.MeetingChatMessageQuery;
import com.easymeeting.entity.query.SimplePage;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.exception.BusinessException;
import com.easymeeting.mappers.MeetingChatMessageMapper;
import com.easymeeting.service.MeetingChatMessageService;
import com.easymeeting.utils.*;
import com.easymeeting.websocket.message.MessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service("meetingChatMessageService")
public class MeetingChatMessageServiceImpl implements MeetingChatMessageService {

    @Resource
    private MeetingChatMessageMapper<MeetingChatMessage, MeetingChatMessageQuery> meetingChatMessageMapper;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private AppConfig appConfig;

    @Resource
    private FFmpegUtils fFmpegUtils;

    @Override
    public List<MeetingChatMessage> findListByParam(MeetingChatMessageQuery param) {
        String tableName = TableSplitUtils.getMeetingChatMessageTable(param.getMeetingId());
        return this.meetingChatMessageMapper.selectListByParam(param, tableName);
    }

    @Override
    public Integer findCountByParam(MeetingChatMessageQuery param) {
        String tableName = TableSplitUtils.getMeetingChatMessageTable(param.getMeetingId());
        return this.meetingChatMessageMapper.selectCountByParam(param, tableName);
    }

    @Override
    public PaginationResultVO<MeetingChatMessage> findListByPage(MeetingChatMessageQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? 15 : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<MeetingChatMessage> list = this.findListByParam(param);
        PaginationResultVO<MeetingChatMessage> vo = new PaginationResultVO<MeetingChatMessage>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return vo;
    }

    @Override
    public Integer add(MeetingChatMessage bean) {
        String tableName = TableSplitUtils.getMeetingChatMessageTable(bean.getMeetingId());
        return this.meetingChatMessageMapper.insert(bean, tableName);
    }

    @Override
    public Integer addBatch(List<MeetingChatMessage> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        String tableName = TableSplitUtils.getMeetingChatMessageTable(listBean.get(0).getMeetingId());
        return this.meetingChatMessageMapper.insertBatch(listBean, tableName);
    }

    @Override
    public Integer addOrUpdateBatch(List<MeetingChatMessage> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        String tableName = TableSplitUtils.getMeetingChatMessageTable(listBean.get(0).getMeetingId());
        return this.meetingChatMessageMapper.insertOrUpdateBatch(listBean, tableName);
    }

    @Override
    public Integer updateByParam(MeetingChatMessage bean, MeetingChatMessageQuery param) {
        String tableName = TableSplitUtils.getMeetingChatMessageTable(param.getMeetingId());
        return this.meetingChatMessageMapper.updateByParam(bean, param, tableName);
    }

    @Override
    public Integer deleteByParam(MeetingChatMessageQuery param) {
        String tableName = TableSplitUtils.getMeetingChatMessageTable(param.getMeetingId());
        return this.meetingChatMessageMapper.deleteByParam(param, tableName);
    }

    @Override
    public MeetingChatMessage getMeetingChatMessageByMessageId(Long messageId, String meetingId) {
        String tableName = TableSplitUtils.getMeetingChatMessageTable(meetingId);
        return this.meetingChatMessageMapper.selectByMessageId(messageId, tableName);
    }

    @Override
    public Integer updateMeetingChatMessageByMessageId(MeetingChatMessage bean, Long messageId, String meetingId) {
        String tableName = TableSplitUtils.getMeetingChatMessageTable(meetingId);
        return this.meetingChatMessageMapper.updateByMessageId(bean, messageId, tableName);
    }

    @Override
    public Integer deleteMeetingChatMessageByMessageId(Long messageId, String meetingId) {
        String tableName = TableSplitUtils.getMeetingChatMessageTable(meetingId);
        return this.meetingChatMessageMapper.deleteByMessageId(messageId, tableName);
    }

    @Override
    public void saveChatMessage(MeetingChatMessage chatMessage) {
        if(!ArrayUtils.contains(new Integer[]{MessageTypeEnum.CHAT_TEXT_MESSAGE.getType(), MessageTypeEnum.CHAT_MEDIA_MESSAGE.getType()}, chatMessage.getMessageType())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        ReceiveTypeEnum receiveTypeEnum = ReceiveTypeEnum.getByType(chatMessage.getReceiveType());

        if(receiveTypeEnum == ReceiveTypeEnum.USER && StringTools.isEmpty(chatMessage.getReceiveUserId())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        MessageTypeEnum messageTypeEnum = MessageTypeEnum.getByType(chatMessage.getMessageType());
        if(messageTypeEnum == MessageTypeEnum.CHAT_TEXT_MESSAGE) {
            if (StringTools.isEmpty(chatMessage.getMessageContent())) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            //文本类型直接设置已发送
            chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
        }
        else if(messageTypeEnum == MessageTypeEnum.CHAT_MEDIA_MESSAGE){
            if(StringTools.isEmpty(chatMessage.getFileName()) || chatMessage.getFileSize() == null || chatMessage.getFileType() == null){
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            chatMessage.setFileSuffix(StringTools.getFileSuffix(chatMessage.getFileName()));
            chatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
        }
        chatMessage.setSendTime(System.currentTimeMillis());
        chatMessage.setMessageId(SnowFlakeUtils.nextId());
        String tableName = TableSplitUtils.getMeetingChatMessageTable(chatMessage.getMeetingId());
        meetingChatMessageMapper.insert(chatMessage, tableName);

        MessageSendDto messageSendDto = CopyTools.copy(chatMessage, MessageSendDto.class);
        if(ReceiveTypeEnum.USER == receiveTypeEnum){
            messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.USER.getType());
            messageHandler.sendMessage(messageSendDto);

            //同时给自己发
            messageSendDto.setReceiveUserId(chatMessage.getSendUserId());
            messageHandler.sendMessage(messageSendDto);
        } else {
            messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.GROUP.getType());
            messageHandler.sendMessage(messageSendDto);
        }


    }

    @Override
    public void uploadFile(MultipartFile file, String currentMeetingId, Long messageId, Long sendTime) throws IOException {
        //根据月份分目录
        String month = DateUtil.format(new Date(sendTime), DateTimePatternEnum.YYYYMM.getPattern());
        String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + month;
        File folderFile = new File(folder);
        if(!folderFile.exists()){
            folderFile.mkdirs();
        }

        String filePath = folder + "/" + messageId;
        String fileName = file.getOriginalFilename();
        String fileSuffix = StringTools.getFileSuffix(fileName);
        FileTypeEnum fileTypeEnum = FileTypeEnum.getBySuffix(fileSuffix);

        if(FileTypeEnum.IMAGE == fileTypeEnum){
           File tempFile = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_TEMP + StringTools.getRandomString(Constants.LENGTH_30));
           file.transferTo(tempFile);
           filePath = filePath + Constants.IMAGE_SUFFIX;
           //类型转.jpg然后生成缩略图
           filePath = fFmpegUtils.transferImageType(tempFile, filePath);
           fFmpegUtils.createImageThumbnail(filePath);
        }else if(FileTypeEnum.VIDEO == fileTypeEnum){
            //转化格式，生成封面
            File tempFile = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_TEMP + StringTools.getRandomString(Constants.LENGTH_30));
            file.transferTo(tempFile);
            filePath = filePath + Constants.VIDEO_SUFFIX;
            fFmpegUtils.transferVideoType(tempFile, filePath, fileSuffix);
            // 生成视频封面（使用图片缩略图命名规则）
            String coverPath = StringTools.getImageThumbnail(filePath);
            fFmpegUtils.createVideoCover(filePath, coverPath);
        }else{
            filePath = filePath + fileSuffix;
            file.transferTo(new File(filePath));
        }

        String tableName = TableSplitUtils.getMeetingChatMessageTable(currentMeetingId);
        MeetingChatMessage meetingChatMessage = new MeetingChatMessage();
        meetingChatMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
        this.meetingChatMessageMapper.updateByMessageId(meetingChatMessage, messageId, tableName);

        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMeetingId(currentMeetingId);
        messageSendDto.setMessageType(MessageTypeEnum.CHAT_MEDIA_MESSAGE_UPDATE.getType());
        messageSendDto.setStatus(MessageStatusEnum.SENDED.getStatus());
        messageSendDto.setMessageId(messageId);
        messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.GROUP.getType());
        messageHandler.sendMessage(messageSendDto);
    }
}

