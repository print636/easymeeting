package com.easymeeting.service.impl;

import com.easymeeting.entity.po.MeetingReserveMember;
import com.easymeeting.entity.query.MeetingReserveMemberQuery;
import com.easymeeting.entity.query.SimplePage;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.mappers.MeetingReserveMemberMapper;
import com.easymeeting.service.MeetingReserveMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("meetingReserveMemberService")
public class MeetingReserveMemberServiceImpl implements MeetingReserveMemberService {

    @Resource
    private MeetingReserveMemberMapper<MeetingReserveMember, MeetingReserveMemberQuery> meetingReserveMemberMapper;

    @Override
    public List<MeetingReserveMember> findListByParam(MeetingReserveMemberQuery param) {
        return this.meetingReserveMemberMapper.selectListByParam(param);
    }

    @Override
    public Integer findCountByParam(MeetingReserveMemberQuery param) {
        return this.meetingReserveMemberMapper.selectCountByParam(param);
    }

    @Override
    public PaginationResultVO<MeetingReserveMember> findListByPage(MeetingReserveMemberQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? 15 : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<MeetingReserveMember> list = this.findListByParam(param);
        PaginationResultVO<MeetingReserveMember> vo = new PaginationResultVO<MeetingReserveMember>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return vo;
    }

    @Override
    public Integer add(MeetingReserveMember bean) {
        return this.meetingReserveMemberMapper.insert(bean);
    }

    @Override
    public Integer addBatch(List<MeetingReserveMember> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        return this.meetingReserveMemberMapper.insertBatch(listBean);
    }

    @Override
    public Integer addOrUpdateBatch(List<MeetingReserveMember> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        return this.meetingReserveMemberMapper.insertOrUpdateBatch(listBean);
    }

    @Override
    public Integer updateByParam(MeetingReserveMember bean, MeetingReserveMemberQuery param) {
        return this.meetingReserveMemberMapper.updateByParam(bean, param);
    }

    @Override
    public Integer deleteByParam(MeetingReserveMemberQuery param) {
        return this.meetingReserveMemberMapper.deleteByParam(param);
    }

    @Override
    public Integer deleteByMeetingIdAndInviteUserId(String meetingId, String inviteUserId) {
        return this.meetingReserveMemberMapper.deleteByMeetingIdAndInviteUserId(meetingId, inviteUserId);
    }
}

