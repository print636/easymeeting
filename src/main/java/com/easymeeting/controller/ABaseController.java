package com.easymeeting.controller;

import com.easymeeting.entity.constants.Constants;
import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.entity.enums.ResponseCodeEnum;
import com.easymeeting.entity.vo.ResponseVO;
import com.easymeeting.exception.BusinessException;
import com.easymeeting.redis.RedisComponent;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class ABaseController {


    protected static final String STATUS_SUCCESS = "success";

    protected static final String STATUS_ERROR = "error";

    @Resource
    private RedisComponent redisComponent;


    protected <T> ResponseVO<T> getSuccessResponseVO(T data) {
        ResponseVO<T> responseVo = new ResponseVO<>();
        responseVo.setStatus(STATUS_SUCCESS);
        responseVo.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVo.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVo.setData(data);
        return responseVo;
    }

    protected <T> ResponseVO<T> getServerErrorResponseVO(T t) {
        ResponseVO<T> responseVo = new ResponseVO<>();
        responseVo.setStatus(STATUS_ERROR);
        responseVo.setCode(ResponseCodeEnum.CODE_500.getCode());
        responseVo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        responseVo.setData(t);
        return responseVo;
    }

    protected <T> ResponseVO<T> getBusinessErrorResponseVO(BusinessException e, T t) {
        ResponseVO vo = new ResponseVO();
        vo.setStatus(STATUS_ERROR);
        if (e.getCode() == null) {
            vo.setCode(ResponseCodeEnum.CODE_600.getCode());
        } else {
            vo.setCode(e.getCode());
        }
        vo.setInfo(e.getMessage());
        vo.setData(t);
        return vo;
    }

    protected TokenUserInfoDto getTokenUserInfo(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDtoByToken(token);
        return tokenUserInfoDto;
    }

    protected TokenUserInfoDto getTokenUserInfo(String token){
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDtoByToken(token);
        return tokenUserInfoDto;
    }

    protected void resetTokenUserInfo(TokenUserInfoDto tokenUserInfoDto){
        redisComponent.saveTokenUserInfoDto(tokenUserInfoDto);
    }
}
