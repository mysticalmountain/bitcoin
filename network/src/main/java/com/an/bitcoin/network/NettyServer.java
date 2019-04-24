package com.an.bitcoin.network;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private int port;

    //TODO use configuration file set netty server setting
    public NettyServer(ChannelInitializer<T> channelInitializer, int port) {
        this.channelInitializer = channelInitializer;
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
                    .childHandler(channelInitializer);
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
