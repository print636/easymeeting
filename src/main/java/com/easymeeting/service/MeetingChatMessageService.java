package com.easymeeting.service;

import com.easymeeting.entity.po.MeetingChatMessage;
import com.easymeeting.entity.query.MeetingChatMessageQuery;
import com.easymeeting.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MeetingChatMessageService {

    List<MeetingChatMessage> findListByParam(MeetingChatMessageQuery param);

    Integer findCountByParam(MeetingChatMessageQuery param);

    PaginationResultVO<MeetingChatMessage> findListByPage(MeetingChatMessageQuery param);

    Integer add(MeetingChatMessage bean);

    Integer addBatch(List<MeetingChatMessage> listBean);

    Integer addOrUpdateBatch(List<MeetingChatMessage> listBean);

    Integer updateByParam(MeetingChatMessage bean, MeetingChatMessageQuery param);

    Integer deleteByParam(MeetingChatMessageQuery param);

    MeetingChatMessage getMeetingChatMessageByMessageId(Long messageId, String meetingId);

    Integer updateMeetingChatMessageByMessageId(MeetingChatMessage bean, Long messageId, String meetingId);

    Integer deleteMeetingChatMessageByMessageId(Long messageId, String meetingId);

    void saveChatMessage(MeetingChatMessage chatMessage);

    void uploadFile(MultipartFile file, String currentMeetingId, Long messageId, Long sendTime) throws IOException;
}

