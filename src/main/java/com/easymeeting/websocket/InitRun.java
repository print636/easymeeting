package com.easymeeting.websocket;

import com.easymeeting.websocket.message.MessageHandler;
import com.easymeeting.websocket.netty.NettyWebSocketStarter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InitRun implements ApplicationRunner {

    @Resource
    private NettyWebSocketStarter nettyWebSocketStarter;

    @Resource
    private MessageHandler messageHandler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(nettyWebSocketStarter).start();

        new Thread(()->{
            messageHandler.listenMessage();
        }).start();
    }
}
