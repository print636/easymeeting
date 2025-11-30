package com.easymeeting.websocket.netty;

import com.easymeeting.config.AppConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class NettyWebSocketStarter implements Runnable{

    //Boss线程，用于处理连接
    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    //Worker线程，用于处理消息
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Resource
    private HandlerTokenValidation handlerTokenValidation;

    @Resource
    private HandlerWebSocket handlerWebSocket;

    @Resource
    private AppConfig appConfig;

    @Override
    public void run(){
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.DEBUG)).
                    childHandler(new ChannelInitializer<Channel>(){
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //对http协议的支持，使用http的编码器,解码器
                            pipeline.addLast(new HttpServerCodec());

                            //这是一个http消息聚合器，主要是将分开的http消息，聚合成完整FullHttpRequest,FullHttpResponse
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));

                            //int readerIdleTimeSeconds, 一段时间未收到客户端数据
                            //int writerIdleTimeSeconds, 一段时间未向客户端发送数据
                            //int allIdleTimeSeconds, 读和写都无活动
                            pipeline.addLast(new IdleStateHandler(6,0,0));
                            pipeline.addLast(new HandlerHeartBeat());

                            //鉴权,token校验，拦截channelRead事件
                            pipeline.addLast(handlerTokenValidation);

                            /*
                             * websocket 协议处理
                             * websocketPath 路径
                             * subprotocols, 指定支持的子协议
                             * allowExtensions, 是否允许扩展
                             * maxFrameSize 设置最大帧数
                             * allowMaskMismatch 是否允许掩码不匹配
                             * checkStartsWith 是否严格检查路径开头
                             * handshakeTimeoutMillis 握手超时时间
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws",null,true,6553,true,true,10000L));
                            //自定义业务处理器
                            pipeline.addLast(handlerWebSocket);
                        }
            });

            Channel channel = serverBootstrap.bind(appConfig.getWsPort()).sync().channel();
            log.info("Netty服务启动成功,端口:{}", appConfig.getWsPort());
            channel.closeFuture().sync();

        } catch (Exception e) {
            log.error("netty启动失败", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }



}
