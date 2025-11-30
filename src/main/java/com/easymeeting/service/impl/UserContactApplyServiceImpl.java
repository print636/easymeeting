package com.easymeeting.service.impl;

import com.easymeeting.entity.dto.MessageSendDto;
import com.easymeeting.entity.enums.*;
import com.easymeeting.entity.po.UserContact;
import com.easymeeting.entity.po.UserContactApply;
import com.easymeeting.entity.query.SimplePage;
import com.easymeeting.entity.query.UserContactApplyQuery;
import com.easymeeting.entity.query.UserContactQuery;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.exception.BusinessException;
import com.easymeeting.mappers.UserContactApplyMapper;
import com.easymeeting.mappers.UserContactMapper;
import com.easymeeting.service.UserContactApplyService;
import com.easymeeting.websocket.message.MessageHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("userContactApplyService")
public class UserContactApplyServiceImpl implements UserContactApplyService {

    @Resource
    private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;

    @Resource
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    @Resource
    private MessageHandler messageHandler;

    @Override
    public List<UserContactApply> findListByParam(UserContactApplyQuery param) {
        return this.userContactApplyMapper.selectListByParam(param);
    }

    @Override
    public Integer findCountByParam(UserContactApplyQuery param) {
        return this.userContactApplyMapper.selectCountByParam(param);
    }

    @Override
    public PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? 15 : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<UserContactApply> list = this.findListByParam(param);
        PaginationResultVO<UserContactApply> vo = new PaginationResultVO<UserContactApply>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return vo;
    }

    @Override
    public Integer add(UserContactApply bean) {
        // apply_id 为自增主键，插入后会自动填充到 bean 对象中
        return this.userContactApplyMapper.insert(bean);
    }

    @Override
    public Integer addBatch(List<UserContactApply> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        return this.userContactApplyMapper.insertBatch(listBean);
    }

    @Override
    public Integer addOrUpdateBatch(List<UserContactApply> listBean) {
        if(listBean == null || listBean.isEmpty()){
            return 0;
        }
        return this.userContactApplyMapper.insertOrUpdateBatch(listBean);
    }

    @Override
    public Integer updateByParam(UserContactApply bean, UserContactApplyQuery param) {
        return this.userContactApplyMapper.updateByParam(bean, param);
    }

    @Override
    public Integer deleteByParam(UserContactApplyQuery param) {
        return this.userContactApplyMapper.deleteByParam(param);
    }

    @Override
    public UserContactApply getUserContactApplyByApplyId(Integer applyId) {
        return this.userContactApplyMapper.selectByApplyId(applyId);
    }

    @Override
    public UserContactApply getUserContactApplyByApplyUserIdAndReceiveUserId(String applyUserId, String receiveUserId) {
        return this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserId(applyUserId, receiveUserId);
    }

    @Override
    public Integer updateUserContactApplyByApplyId(UserContactApply bean, Integer applyId) {
        return this.userContactApplyMapper.updateByApplyId(bean, applyId);
    }

    @Override
    public Integer updateUserContactApplyByApplyUserIdAndReceiveUserId(UserContactApply bean, String applyUserId, String receiveUserId) {
        return this.userContactApplyMapper.updateByApplyUserIdAndReceiveUserId(bean, applyUserId, receiveUserId);
    }

    @Override
    public Integer deleteUserContactApplyByApplyId(Integer applyId) {
        return this.userContactApplyMapper.deleteByApplyId(applyId);
    }

    @Override
    public Integer deleteUserContactApplyByApplyUserIdAndReceiveUserId(String applyUserId, String receiveUserId) {
        return this.userContactApplyMapper.deleteByApplyUserIdAndReceiveUserId(applyUserId, receiveUserId);
    }

    @Override
    public Integer saveUserContactApply(UserContactApply bean) {
        UserContact userContact = this.userContactMapper.selectByUserIdAndContactId(bean.getReceiveUserId(), bean.getApplyUserId());
        if(userContact != null && UserContactStatusEnum.BLACKLIST.getStatus().equals(userContact.getStatus())){
            throw new BusinessException("对方已将你加入黑名单");
        }
        //自己误将对方删除情况
        if(userContact != null && UserContactStatusEnum.FRIEND.getStatus().equals(userContact.getStatus())){
            UserContact updateInfo = new UserContact();
            updateInfo.setLastUpdateTime(new Date());
            updateInfo.setStatus(UserContactStatusEnum.FRIEND.getStatus());
            userContactMapper.updateByUserIdAndContactId(updateInfo, bean.getReceiveUserId(), bean.getApplyUserId());
            return UserContactStatusEnum.FRIEND.getStatus();
        }

        UserContactApply apply = this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserId(bean.getApplyUserId(), bean.getReceiveUserId());

        if(apply == null){
            bean.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
            bean.setLastApplyTime(new Date());
            this.userContactApplyMapper.insert(bean);
        } else{
            UserContactApply updateInfo = new UserContactApply();
            updateInfo.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
            updateInfo.setLastApplyTime(new Date());
            this.userContactApplyMapper.updateByApplyId(updateInfo, apply.getApplyId());
        }

        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.USER.getType());
        messageSendDto.setMessageType(MessageTypeEnum.USER_CONTACT_APPLY.getType());
        messageSendDto.setReceiveUserId(bean.getReceiveUserId());
        messageHandler.sendMessage(messageSendDto);
        return UserContactApplyStatusEnum.INIT.getStatus();
    }

    @Override
    public void dealWithApply(String applyUserId, String userId, String nickName, Integer status) {
        UserContactApplyStatusEnum statusEnum = UserContactApplyStatusEnum.getByStatus(status);
        if(statusEnum == null || UserContactApplyStatusEnum.INIT == statusEnum){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserContactApply apply = this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserId(applyUserId, userId);
        if(apply == null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if(UserContactApplyStatusEnum.PASS.getStatus().equals(status)){
            Date now = new Date();
            UserContact userContact = new UserContact();
            userContact.setContactId(userId);
            userContact.setUserId(applyUserId);
            userContact.setStatus(UserContactStatusEnum.FRIEND.getStatus());
            userContact.setLastUpdateTime(now);
            this.userContactMapper.insertOrUpdate(userContact);

            //对方也添加自己
            userContact.setUserId(userId);
            userContact.setContactId(applyUserId);
            this.userContactMapper.insertOrUpdate(userContact);
        }

        UserContactApply updateInfo = new UserContactApply();
        updateInfo.setStatus(status);
        this.userContactApplyMapper.updateByApplyId(updateInfo, apply.getApplyId());

        MessageSendDto sendDto = new MessageSendDto();
        sendDto.setMessageSend2Type(MessageSend2TypeEnum.USER.getType());
        sendDto.setMessageType(MessageTypeEnum.USER_CONTACT_DEAL_WITH.getType());
        sendDto.setReceiveUserId(applyUserId);
        sendDto.setSendUserNickName(nickName);
        sendDto.setMessageContent(status);
        messageHandler.sendMessage(sendDto);
   }


}

