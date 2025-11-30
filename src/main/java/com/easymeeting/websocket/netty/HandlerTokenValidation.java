package com.easymeeting.websocket.netty;

import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.redis.RedisComponent;
import com.easymeeting.utils.StringTools;
import com.easymeeting.websocket.ChannelContextUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.MemberSubstitution;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@ChannelHandler.Sharable
@Slf4j
public class HandlerTokenValidation extends SimpleChannelInboundHandler<FullHttpRequest>{

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private ChannelContextUtils channelContextUtils;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        List<String> tokens = queryStringDecoder.parameters().get("token");
        if(tokens == null){
            sendErrorResponse(ctx);
            return;
        }
        String token = tokens.get(0);
        TokenUserInfoDto tokenUserInfoDto = checkToken(token);
        if(tokenUserInfoDto == null){
            log.error("校验token失败{}",token);
            sendErrorResponse(ctx);
            return;
        }
        ctx.fireChannelRead(request.retain());
        //连接成功后的初始化工作
        channelContextUtils.addContext(tokenUserInfoDto.getUserId(), ctx.channel());

    }

    private TokenUserInfoDto checkToken(String token){
        if(StringTools.isEmpty(token)){
            return null;
        }
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDtoByToken(token);
        return tokenUserInfoDto;
    }

    private void sendErrorResponse(ChannelHandlerContext ctx){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.copiedBuffer("token校验失败", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


}
