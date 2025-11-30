package com.easymeeting.service.impl;

import com.easymeeting.entity.po.MeetingMember;
import com.easymeeting.entity.query.MeetingMemberQuery;
import com.easymeeting.entity.query.SimplePage;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.mappers.MeetingMemberMapper;
import com.easymeeting.service.MeetingMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("meetingMemberService")
public class MeetingMemberServiceImpl implements MeetingMemberService {

    @Resource
    private MeetingMemberMapper<MeetingMember, MeetingMemberQuery> meetingMemberMapper;

    @Override
    public List<MeetingMember> findListByParam(MeetingMemberQuery param) {
        return this.meetingMemberMapper.selectListByParam(param);
    }

    @Override
    public Integer findCountByParam(MeetingMemberQuery param) {
        return this.meetingMemberMapper.selectCountByParam(param);
    }

    @Override
    public PaginationResultVO<MeetingMember> findListByPage(MeetingMemberQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? 15 : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<MeetingMember> list = this.findListByParam(param);
        PaginationResultVO<MeetingMember> vo = new PaginationResultVO<MeetingMember>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return vo;
    }

    @Override
    public Integer add(MeetingMember bean) {
        return this.meetingMemberMapper.insert(bean);
    }

    @Override
    public Integer addBatch(List<MeetingMember> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        return this.meetingMemberMapper.insertBatch(listBean);
    }

    @Override
    public Integer addOrUpdateBatch(List<MeetingMember> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        return this.meetingMemberMapper.insertOrUpdateBatch(listBean);
    }

    @Override
    public Integer updateByParam(MeetingMember bean, MeetingMemberQuery param) {
        return this.meetingMemberMapper.updateByParam(bean, param);
    }

    @Override
    public Integer deleteByParam(MeetingMemberQuery param) {
        return this.meetingMemberMapper.deleteByParam(param);
    }
}

