package com.easymeeting.service;

import com.easymeeting.entity.po.MeetingReserve;
import com.easymeeting.entity.query.MeetingReserveQuery;
import com.easymeeting.entity.vo.PaginationResultVO;

import java.util.List;

public interface MeetingReserveService {

    List<MeetingReserve> findListByParam(MeetingReserveQuery param);

    Integer findCountByParam(MeetingReserveQuery param);

    PaginationResultVO<MeetingReserve> findListByPage(MeetingReserveQuery param);

    Integer add(MeetingReserve bean);

    Integer addBatch(List<MeetingReserve> listBean);

    Integer addOrUpdateBatch(List<MeetingReserve> listBean);

    Integer updateByParam(MeetingReserve bean, MeetingReserveQuery param);

    Integer deleteByParam(MeetingReserveQuery param);

    MeetingReserve getMeetingReserveByMeetingId(String meetingId);

    Integer updateMeetingReserveByMeetingId(MeetingReserve bean, String meetingId);

    Integer deleteMeetingReserveByMeetingId(String meetingId);

    void createMeetingReserve(MeetingReserve meetingReserve);

    void deleteMeetingReserve(String meetingId, String userId);

    void deleteMeetingReserveByUser(String meetingId, String userId);
}

