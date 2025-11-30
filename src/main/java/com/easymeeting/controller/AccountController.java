package com.easymeeting.controller;

import com.easymeeting.entity.dto.SysSettingDto;
import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.entity.po.UserInfo;
import com.easymeeting.entity.vo.CheckCodeVO;
import com.easymeeting.entity.vo.ResponseVO;
import com.easymeeting.entity.vo.UserInfoVO;
import com.easymeeting.exception.BusinessException;
import com.easymeeting.redis.RedisComponent;
import com.easymeeting.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;

@RestController
@RequestMapping("/account")
@Validated
@Slf4j
public class AccountController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/checkCode")
    public ResponseVO checkCode(){
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        String code = captcha.text();
        String checkCodeKey = redisComponent.saveCheckCode(code);
        String checkCodeBase64 = captcha.toBase64();
        log.info("code: {}", code);

        CheckCodeVO checkCodeVO = new CheckCodeVO();
        checkCodeVO.setCheckCode(checkCodeBase64);
        checkCodeVO.setCheckCodeKey(checkCodeKey);

        return getSuccessResponseVO(checkCodeVO);
    }
    
    @RequestMapping("/register")
    public ResponseVO register(
            @NotEmpty String checkCodeKey,
            @NotEmpty @Email String email,
            @NotEmpty @Size(max = 20) String password,
            @NotEmpty @Size(max = 20) String nickName,
            @NotEmpty String checkCode
    ){
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
                throw new BusinessException("图片验证码不正确");
            }
            this.userInfoService.register(email, nickName, password);
            return getSuccessResponseVO(null);
        }finally {

            redisComponent.cleanCheckCode(checkCodeKey);
        }

    }

    @RequestMapping("/login")
    public ResponseVO login(
            @NotEmpty String checkCodeKey,
            @NotEmpty @Email String email,
            @NotEmpty @Size(max = 32) String password,
            @NotEmpty String checkCode
    ){
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
                throw new BusinessException("图片验证码不正确");
            }
            UserInfoVO userInfoVO = this.userInfoService.login(email, password);
            return getSuccessResponseVO(null);
        }finally {
            redisComponent.cleanCheckCode(checkCodeKey);
        }

    }

    @RequestMapping("/getSysSetting")
    public ResponseVO getSysSetting(){
        SysSettingDto sysSettingDto = redisComponent.getSysSetting();
        return getSuccessResponseVO(sysSettingDto);
    }

    @RequestMapping("/updateUserInfo")
    public ResponseVO updateUserInfo(MultipartFile avatar, @NotEmpty String nickName, @NotNull Integer sex) throws IOException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(nickName);
        userInfo.setSex(sex);
        userInfo.setUserId(tokenUserInfoDto.getUserId());
        userInfoService.updateUserInfo(avatar, userInfo);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/updatePassword")
    public ResponseVO updatePassword(@NotEmpty String oldPassword, @NotEmpty String password){
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo();
        userInfoService.updatePassword(tokenUserInfoDto.getUserId(), oldPassword, password);
        return getSuccessResponseVO(null);
    }

}
