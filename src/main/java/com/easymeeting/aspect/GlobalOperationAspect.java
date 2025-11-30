package com.easymeeting.aspect;

import com.easymeeting.annotation.GlobalInterceptor;
import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.entity.enums.ResponseCodeEnum;
import com.easymeeting.exception.BusinessException;
import com.easymeeting.redis.RedisComponent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.zip.CheckedInputStream;

@Component
@Aspect
@Slf4j
public class GlobalOperationAspect {

    @Resource
    private RedisComponent redisComponent;

    @Before("@annotation(com.easymeeting.annotation.GlobalInterceptor)")
    public void interceptorDo(JoinPoint point){

        try {
            Method method = ((MethodSignature)point.getSignature()).getMethod();
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if(interceptor == null){
                return;
            }
            if(interceptor.checkLogin() || interceptor.checkAdmin()){
                checkLogin(interceptor.checkAdmin());
            }
        } catch (BusinessException e) {
            log.error("全局拦截器异常",e);
            throw e;
        } catch (Exception e) {
            log.error("全部拦截器异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }


    }

    private void checkLogin(Boolean checkAdmin){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDtoByToken(token);
        if(tokenUserInfoDto == null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        if(checkAdmin && !tokenUserInfoDto.getAdmin()){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

    }
}
