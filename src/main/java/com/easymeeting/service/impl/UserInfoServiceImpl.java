package com.easymeeting.service.impl;

import com.easymeeting.config.AppConfig;
import com.easymeeting.entity.constants.Constants;
import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.entity.dto.MessageSendDto;
import com.easymeeting.entity.enums.MessageSend2TypeEnum;
import com.easymeeting.entity.enums.MessageTypeEnum;
import com.easymeeting.entity.enums.ResponseCodeEnum;
import com.easymeeting.entity.enums.UserStatusEnum;
import com.easymeeting.entity.po.UserInfo;
import com.easymeeting.entity.query.SimplePage;
import com.easymeeting.entity.query.UserInfoQuery;
import com.easymeeting.entity.vo.PaginationResultVO;
import com.easymeeting.entity.vo.UserInfoVO;
import com.easymeeting.exception.BusinessException;
import com.easymeeting.mappers.UserInfoMapper;
import com.easymeeting.redis.RedisComponent;
import com.easymeeting.service.UserInfoService;
import com.easymeeting.utils.CopyTools;
import com.easymeeting.utils.FFmpegUtils;
import com.easymeeting.utils.StringTools;
import com.easymeeting.websocket.message.MessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper<UserInfo , UserInfoQuery> userInfoMapper;
    @Resource
    private AppConfig appConfig;
    @Resource
    private RedisComponent redisComponent;

    @Resource
    private MessageHandler messageHandler;
    @Resource
    private FFmpegUtils fFmpegUtils;

    @Override
    public List<UserInfo> findListByParam(UserInfoQuery param) {
        return this.userInfoMapper.selectListByParam(param);
    }

    @Override
    public Integer findCountByParam(UserInfoQuery param) {
        return this.userInfoMapper.selectCountByParam(param);
    }

    @Override
    public PaginationResultVO<UserInfo> findListByPage(UserInfoQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? 15 : param.getPageSize();
        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<UserInfo> list = this.findListByParam(param);
        PaginationResultVO<UserInfo> vo = new PaginationResultVO<UserInfo>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return vo;
    }

    @Override
    public Integer add(UserInfo bean) {
        return this.userInfoMapper.insert(bean);
    }

    @Override
    public Integer addBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoMapper.insertBatch(listBean);
    }

    @Override
    public Integer addOrUpdateBatch(List<UserInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userInfoMapper.insertOrUpdateBatch(listBean);
    }

    @Override
    public Integer updateByParam(UserInfo bean, UserInfoQuery param) {
        return this.userInfoMapper.updateByParam(bean, param);
    }

    @Override
    public Integer deleteByParam(UserInfoQuery param) {
        return this.userInfoMapper.deleteByParam(param);
    }

    @Override
    public UserInfo getUserInfoByUserId(String userId) {
        return this.userInfoMapper.selectByUserId(userId);
    }

    @Override
    public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
        return this.userInfoMapper.updateByUserId(bean, userId);
    }

    @Override
    public Integer deleteUserInfoByUserId(String userId) {
        return this.userInfoMapper.deleteByUserId(userId);
    }

    @Override
    public Integer updateUserInfoByEmail(UserInfo bean, String email) {
        return this.userInfoMapper.updateByEmail(bean, email);
    }

    @Override
    public Integer deleteUserInfoByEmail(String email) {
        return this.userInfoMapper.deleteByEmail(email);
    }

    @Override
    public void register(String email, String nickName, String password) {
        UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
        if(userInfo != null){
            throw new BusinessException("邮箱账号已经存在");
        }
        Date curDate = new Date();
        String userId = StringTools.getRandomNumber(Constants.LENGTH_12);
        userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setNickName(nickName);
        userInfo.setEmail(email);
        userInfo.setPassword(StringTools.encodeByMD5(password));
        userInfo.setCreateTime(curDate);
        userInfo.setLastOffTime(curDate.getTime());
        userInfo.setMeetingNo(StringTools.getMeetingNoOrMeetingId());
        userInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        this.userInfoMapper.insert(userInfo);
    }

    @Override
    public UserInfoVO login(String email, String password) {
        UserInfo userInfo = this.userInfoMapper.selectByEmail(email);

        if(null == userInfo || !userInfo.getPassword().equals(StringTools.encodeByMD5(password))){
            throw new BusinessException("账号或密码错误");
        }
        if(UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())){
            throw new BusinessException("账号已被禁用");
        }

        // 检查是否已在别处登录
        if(userInfo.getLastLoginTime() != null && userInfo.getLastOffTime() <= userInfo.getLastLoginTime()){
            throw new BusinessException("此账号已在别处登录，请退出后再登录");
        }

        // 更新登录时间
        long now = System.currentTimeMillis();
        UserInfo update = new UserInfo();
        update.setLastLoginTime(now);
        this.userInfoMapper.updateByUserId(update, userInfo.getUserId());

        TokenUserInfoDto tokenUserInfoDto = CopyTools.copy(userInfo, TokenUserInfoDto.class);

        String token = StringTools.encodeByMD5(tokenUserInfoDto.getUserId()+StringTools.getRandomString(Constants.LENGTH_20));
        tokenUserInfoDto.setToken(token);
        tokenUserInfoDto.setMyMeetingNo(userInfo.getMeetingNo());
        tokenUserInfoDto.setAdmin(appConfig.getAdminEmails().contains(email));

        redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);

        UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
        userInfoVO.setToken(token);
        userInfoVO.setAdmin(tokenUserInfoDto.getAdmin());

        return userInfoVO;
    }

    @Override
    public void updateUserStatus(Integer status, String userId) {
        UserStatusEnum statusEnum = UserStatusEnum.getByStatus(status);
        if (statusEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        UserInfo update = new UserInfo();
        update.setStatus(statusEnum.getStatus());
        userInfoMapper.updateByUserId(update, userId);

        // 禁用账号时强制下线
        if (UserStatusEnum.DISABLE == statusEnum) {
            forceOffline(userId);
        }
    }

    @Override
    public void forceOffline(String userId) {
        UserInfo userInfo = this.userInfoMapper.selectByUserId(userId);
        if(Constants.ZERO.equals(userInfo.getOnLineType())){
            return;
        }
        // 如果在线
        // 更新用户最后离线时间
        long now = System.currentTimeMillis();
        UserInfo update = new UserInfo();
        update.setLastOffTime(now);
        userInfoMapper.updateByUserId(update, userId);

        // 发送强制下线通知
        MessageSendDto dto = new MessageSendDto();
        dto.setMessageSend2Type(MessageSend2TypeEnum.USER.getType());
        dto.setReceiveUserId(userId);
        dto.setMessageType(MessageTypeEnum.FORCE_OFF_LINE.getType());
        messageHandler.sendMessage(dto);

        // 清理用户 token，强制下线
        redisComponent.cleanTokenByUserId(userId);
    }

    @Override
    public void updateUserInfo(MultipartFile avatar, UserInfo userInfo) throws IOException {
        if(avatar != null){
            String folder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_AVATAR_NANE;
            File folderFile = new File(folder);
            if(!folderFile.exists()){
                folderFile.mkdirs();
            }
            String realFileName = userInfo.getUserId() + Constants.IMAGE_SUFFIX;
            String filePath = folder + realFileName;
            File tempFile = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_TEMP + StringTools.getRandomString(Constants.LENGTH_30));
            avatar.transferTo(tempFile);
            //头像缩略图
            fFmpegUtils.createImageThumbnail(tempFile, filePath);
        }
        this.userInfoMapper.updateByUserId(userInfo, userInfo.getUserId());

        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDtoByUserId(userInfo.getUserId());
        tokenUserInfoDto.setNickName(userInfo.getNickName());
        tokenUserInfoDto.setSex(userInfo.getSex());
        redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);
    }

    @Override
    public void updatePassword(String userId, String oldPassword, String newPassword) {
        UserInfo userInfo = this.userInfoMapper.selectByUserId(userId);
        if(null == userInfo){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if(!userInfo.getPassword().equals(StringTools.encodeByMD5(oldPassword))){
            throw new BusinessException("旧密码错误");
        }
        UserInfo updateInfo = new UserInfo();
        updateInfo.setPassword(StringTools.encodeByMD5(newPassword));
        this.userInfoMapper.updateByUserId(updateInfo, userId);
        redisComponent.cleanTokenByUserId(userId);
    }

}
