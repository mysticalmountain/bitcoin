package com.an.bitcoin.network;

import com.an.bitcoin.core.MessageFactory;
import com.an.bitcoin.core.Peer;
import com.an.bitcoin.core.PeerFactory;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @ClassName NettyServer
 * @Description NettyServer
 * @Author an
 * @Date 2019/4/22 上午9:53
 * @Version 1.0
 */
public class NettyServer<T extends Channel> extends AbstractExecutionThreadService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ChannelInitializer<T> channelInitializer;
    private PeerFactory peerFactory;
    private MessageFactory messageFactory;
    private int port;


    //TODO use configuration file set netty server setting
    public NettyServer(ChannelInitializer<T> channelInitializer, int port) {
        this.channelInitializer = channelInitializer;
        this.port = port;
    }

    public NettyServer(PeerFactory peerFactory, MessageFactory messageFactory, int port) {
        this.peerFactory = peerFactory;
        this.messageFactory = messageFactory;
        this.port = port;
    }


    @Override
    protected void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("MessageDecoder", new MessageDecoder(messageFactory));
                            pipeline.addLast("MessageEncoder", new MessageEncoder());
                            pipeline.addLast("PeerHandler", new PeerHandler(peerFactory));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("Server start at {} port", port);
                    } else {
                        logger.info("Server start failed");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
