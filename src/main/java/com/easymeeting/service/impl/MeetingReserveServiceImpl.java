package com.easymeeting.service.impl;

import com.easymeeting.entity.enums.MeetingReserveStatusEnum;
import com.easymeeting.entity.enums.ResponseCodeEnum;
import com.easymeeting.entity.po.MeetingReserve;
import com.easymeeting.entity.po.MeetingReserveMember;
import com.easymeeting.entity.query.MeetingReserveMemberQuery;
import com.easymeeting.entity.query.MeetingReserveQuery;
import com.easymeeting.entity.query.SimplePage;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.exception.BusinessException;
import com.easymeeting.mappers.MeetingReserveMapper;
import com.easymeeting.mappers.MeetingReserveMemberMapper;
import com.easymeeting.service.MeetingReserveMemberService;
import com.easymeeting.service.MeetingReserveService;
import com.easymeeting.utils.StringTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("meetingReserveService")
public class MeetingReserveServiceImpl implements MeetingReserveService {

    @Resource
    private MeetingReserveMapper<MeetingReserve, MeetingReserveQuery> meetingReserveMapper;

    @Resource
    private MeetingReserveMemberMapper<MeetingReserveMember, MeetingReserveMemberQuery> meetingReserveMemberMapper;

    @Override
    public List<MeetingReserve> findListByParam(MeetingReserveQuery param) {
        return this.meetingReserveMapper.selectListByParam(param);
    }

    @Override
    public Integer findCountByParam(MeetingReserveQuery param) {
        return this.meetingReserveMapper.selectCountByParam(param);
    }

    @Override
    public PaginationResultVO<MeetingReserve> findListByPage(MeetingReserveQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? 15 : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<MeetingReserve> list = this.findListByParam(param);
        PaginationResultVO<MeetingReserve> vo = new PaginationResultVO<MeetingReserve>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return vo;
    }

    @Override
    public Integer add(MeetingReserve bean) {
        return this.meetingReserveMapper.insert(bean);
    }

    @Override
    public Integer addBatch(List<MeetingReserve> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        return this.meetingReserveMapper.insertBatch(listBean);
    }

    @Override
    public Integer addOrUpdateBatch(List<MeetingReserve> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        return this.meetingReserveMapper.insertOrUpdateBatch(listBean);
    }

    @Override
    public Integer updateByParam(MeetingReserve bean, MeetingReserveQuery param) {
        return this.meetingReserveMapper.updateByParam(bean, param);
    }

    @Override
    public Integer deleteByParam(MeetingReserveQuery param) {
        return this.meetingReserveMapper.deleteByParam(param);
    }

    @Override
    public MeetingReserve getMeetingReserveByMeetingId(String meetingId) {
        return this.meetingReserveMapper.selectByMeetingId(meetingId);
    }

    @Override
    public Integer updateMeetingReserveByMeetingId(MeetingReserve bean, String meetingId) {
        return this.meetingReserveMapper.updateByMeetingId(bean, meetingId);
    }

    @Override
    public Integer deleteMeetingReserveByMeetingId(String meetingId) {
        return this.meetingReserveMapper.deleteByMeetingId(meetingId);
    }

    @Override
    public void createMeetingReserve(MeetingReserve bean) {
        bean.setMeetingId(StringTools.getMeetingNoOrMeetingId());
        bean.setCreateTime(new Date());
        bean.setStatus(MeetingReserveStatusEnum.NO_START.getStatus());
        this.meetingReserveMapper.insert(bean);
        List<MeetingReserveMember> meetingReserveMemberList = new ArrayList<>();
        if(!StringTools.isEmpty(bean.getInviteUserIds())) {
            String[] inviteUserIdArray = bean.getInviteUserIds().split(",");
            for (String userId : inviteUserIdArray) {
                MeetingReserveMember member = new MeetingReserveMember();
                member.setMeetingId(bean.getMeetingId());
                member.setInviteUserId(userId);
                meetingReserveMemberList.add(member);
            }
        }
            MeetingReserveMember member = new MeetingReserveMember();
            member.setMeetingId(bean.getMeetingId());
            member.setInviteUserId(bean.getCreateUserId());
            meetingReserveMemberList.add(member);
            meetingReserveMemberMapper.insertBatch(meetingReserveMemberList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMeetingReserve(String meetingId, String userId) {
        MeetingReserveQuery reserveQuery = new MeetingReserveQuery();
        reserveQuery.setMeetingId(meetingId);
        reserveQuery.setCreateUserId(userId);
        Integer count = this.meetingReserveMapper.deleteByParam(reserveQuery);
        if(count > 0){
            MeetingReserveMemberQuery memberQuery = new MeetingReserveMemberQuery();
            memberQuery.setMeetingId(meetingId);
            this.meetingReserveMemberMapper.deleteByParam(memberQuery);
        }
    }

    @Override
    public void deleteMeetingReserveByUser(String meetingId, String userId) {
        MeetingReserve meetingReserve = this.meetingReserveMapper.selectByMeetingId(meetingId);
        if(meetingReserve == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if(meetingReserve.getCreateUserId().equals(userId)){
            deleteMeetingReserve(meetingId, userId);
        } else {
            MeetingReserveMemberQuery memberQuery = new MeetingReserveMemberQuery();
            memberQuery.setMeetingId(meetingId);
            memberQuery.setInviteUserId(userId);
            this.meetingReserveMemberMapper.deleteByParam(memberQuery);
        }

    }

}

