package com.an.bitcoin.network;

import com.an.bitcoin.protocol.Message;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @ClassName NettyClient
 * @Description NettyClient
 * @Author an
 * @Date 2019/4/22 下午5:03
 * @Version 1.0
 */
public class NettyClient<T extends Channel> extends AbstractExecutionThreadService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String ip;
    private int port;
    private PeerHandler peerHandler;
    private SettableFuture<Void> startedFuture = SettableFuture.create();

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        peerHandler = new PeerHandler();
    }

    @Override
    protected void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("MessageDecoder", new MessageDecoder());
                        pipeline.addLast("MessageEncoder", new MessageEncoder());
                        pipeline.addLast("PeerHandler", peerHandler);
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.info("Netty client start successed, connect to {} port {}", ip, port);
                    startedFuture.set(null);
                } else {
                    logger.error("Netty client start failed exit");
                    System.exit(-1);
                }
            }
        });
    }

    public void writeMessage(Message message) {
        peerHandler.writeMessage(message);
    }

    class PeerHandler extends SimpleChannelInboundHandler<Message> {

        private ChannelHandlerContext ctx;

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
            logger.info("receive message {}", msg);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            this.ctx = ctx;
            logger.info("Channel active");
        }

        public void writeMessage(Message message) {
            try {
                startedFuture.get();
            } catch (Exception e) {
                logger.error("Unknown exception {}", e);
            }
            logger.info("Write message {}", message);
            ctx.writeAndFlush(message);
        }
    }
}
