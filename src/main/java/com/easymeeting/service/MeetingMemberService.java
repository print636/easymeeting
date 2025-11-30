package com.easymeeting.service;

import com.easymeeting.entity.po.MeetingMember;
import com.easymeeting.entity.query.MeetingMemberQuery;
import com.easymeeting.entity.vo.PaginationResultVO;

import java.util.List;

public interface MeetingMemberService {

    List<MeetingMember> findListByParam(MeetingMemberQuery param);

    Integer findCountByParam(MeetingMemberQuery param);

    PaginationResultVO<MeetingMember> findListByPage(MeetingMemberQuery param);

    Integer add(MeetingMember bean);

    Integer addBatch(List<MeetingMember> listBean);

    Integer addOrUpdateBatch(List<MeetingMember> listBean);

    Integer updateByParam(MeetingMember bean, MeetingMemberQuery param);

    Integer deleteByParam(MeetingMemberQuery param);
}

