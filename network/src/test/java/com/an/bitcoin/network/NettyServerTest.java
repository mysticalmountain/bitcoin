package com.an.bitcoin.network;

import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName NettyServerTest
 * @Description NettyServerTest
 * @Author an
 * @Date 2019/4/22 下午4:57
 * @Version 1.0
 */
public class NettyServerTest {

    private static Logger logger = LoggerFactory.getLogger(NettyServerTest.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Test Netty server");
        NettyServer<SocketChannel> nettyServer = new NettyServer<SocketChannel>(new PeerInitializer(), 4002);
        NettyServerTest nettyServerTest = new NettyServerTest();
        nettyServerTest.startUp(nettyServer);
    }

    public void startUp(NettyServer server) {
        server.startAsync();
        logger.info("Test Netty server started");

    }

    public void stop(NettyServer server) {
        logger.info("Test Netty server stoped");
        server.stopAsync();
        System.exit(-1);
    }
}
