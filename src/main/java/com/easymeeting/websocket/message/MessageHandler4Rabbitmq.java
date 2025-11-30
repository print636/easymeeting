package com.easymeeting.websocket.message;

import com.easymeeting.entity.constants.Constants;
import com.easymeeting.entity.dto.MessageSendDto;
import com.easymeeting.utils.JsonUtils;
import com.easymeeting.websocket.ChannelContextUtils;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnProperty(name = Constants.MESSAGING_HANDLE_CHANNEL_KEY, havingValue = Constants.MESSAGING_HANDLE_CHANNEL_RABBITMQ)
@Slf4j
public class MessageHandler4Rabbitmq implements MessageHandler{

    private static final String EXCHANGE_NAME = "fanout_exchange";

    private static final Integer MAX_RETRY_TIMES = 3;

    private static final String RETRY_COUNT_KEY = "retryCount";

    @Value("${rabbitmq.host:}")
    private String host;

    @Value("${rabbitmq.port:}")
    private Integer port;

    @Resource
    private ChannelContextUtils channelContextUtils;

    private ConnectionFactory factory;

    private Connection connection;
    private Channel channel;

    @Override
    public void listenMessage() {
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        try{
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");
            Boolean autoAck = false;
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    log.info("rabbitmq收到消息:{}", message);
                    channelContextUtils.sendMessage(JsonUtils.convertJson2Obj(message, MessageSendDto.class));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e){
                    log.info("处理消息失败", e);
                    handlerFailMessage(channel, delivery, queueName);
                }
            };

            channel.basicConsume(queueName, autoAck, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            log.error("rabbitmq监听消息失败", e);
        }
    }

    private void handlerFailMessage(Channel channel, Delivery delivery, String queueName) throws IOException {
        Map<String, Object> headers = delivery.getProperties().getHeaders();
        if(headers == null){
            headers = new HashMap<>();
        }
        Integer retryCount = 0;
        if(headers.containsKey(RETRY_COUNT_KEY)){
            retryCount = (Integer) headers.get(RETRY_COUNT_KEY);
        }
        if(retryCount < MAX_RETRY_TIMES - 1){
            headers.put(RETRY_COUNT_KEY, retryCount + 1);
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().headers((headers)).build();
            channel.basicPublish("", queueName, properties, delivery.getBody());
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        } else {
            log.info("消息重试次数超过最大次数，放弃处理");
            channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }


    @Override
    public void sendMessage(MessageSendDto messageSendDto) {
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.basicPublish(EXCHANGE_NAME, "", null, JsonUtils.convertObj2Json(messageSendDto).getBytes("UTF-8"));
        } catch (Exception e){
            log.error("rabbitmq发送消息失败", e);
        }
    }

    @PreDestroy
    public void destroy() throws Exception {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if(connection != null && connection.isOpen()){
            connection.close();
        }
    }
}
