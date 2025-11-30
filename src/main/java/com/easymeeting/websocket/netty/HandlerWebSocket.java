package com.easymeeting.websocket.netty;

import com.easymeeting.entity.constants.Constants;
import com.easymeeting.entity.dto.MessageSendDto;
import com.easymeeting.entity.dto.PeerConnectionDataDto;
import com.easymeeting.entity.dto.PeerMessageDto;
import com.easymeeting.entity.dto.TokenUserInfoDto;
import com.easymeeting.entity.enums.MessageSend2TypeEnum;
import com.easymeeting.entity.enums.MessageTypeEnum;
import com.easymeeting.entity.po.UserInfo;
import com.easymeeting.entity.query.UserInfoQuery;
import com.easymeeting.mappers.UserInfoMapper;
import com.easymeeting.redis.RedisComponent;
import com.easymeeting.utils.JsonUtils;
import com.easymeeting.websocket.message.MessageHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@ChannelHandler.Sharable
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private MessageHandler messageHandler;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有新的连接加入......");

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("有连接已经断开......");
        // 处理连接断开的逻辑：更新用户离线时间
        Attribute<String> attribute = ctx.channel().attr(AttributeKey.valueOf(ctx.channel().id().toString()));
        String userId = attribute.get();
        if(userId != null){
            UserInfo updateInfo = new UserInfo();
            updateInfo.setLastOffTime(System.currentTimeMillis());
            userInfoMapper.updateByUserId(updateInfo, userId);
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text =  textWebSocketFrame.text();
        if(Constants.PING.equals(text)){
            return;
        }
        log.error("收到消息:{}",text);

        PeerConnectionDataDto dataData = JsonUtils.convertJson2Obj(text, PeerConnectionDataDto.class);
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDtoByToken(dataData.getToken());
        if(tokenUserInfoDto == null){
            return;
        }
        MessageSendDto messageSendDto = new MessageSendDto();
        messageSendDto.setMessageType(MessageTypeEnum.PEER.getType());

        PeerMessageDto peerMessageDto = new PeerMessageDto();
        peerMessageDto.setSignalType(dataData.getSignalType());
        peerMessageDto.setSignalData(dataData.getSignalData());

        messageSendDto.setMessageContent(peerMessageDto);
        messageSendDto.setMeetingId(tokenUserInfoDto.getCurrentMeetingId());
        messageSendDto.setSendUserId(tokenUserInfoDto.getUserId());
        messageSendDto.setReceiveUserId(dataData.getReceiveUserId());
        messageSendDto.setMessageSend2Type(MessageSend2TypeEnum.GROUP.getType());

        messageHandler.sendMessage(messageSendDto);
    }
}

