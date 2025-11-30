package com.easymeeting.websocket.message;


import com.easymeeting.entity.dto.MessageSendDto;
import org.springframework.stereotype.Component;

@Component("messageHandler")
public interface MessageHandler {
    void listenMessage();

    void sendMessage(MessageSendDto messageSendDto);
}
