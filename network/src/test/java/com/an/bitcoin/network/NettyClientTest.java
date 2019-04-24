package com.an.bitcoin.network;

import com.an.bitcoin.protocol.Message;
import com.an.bitcoin.protocol.Ping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @ClassName NettyClientTest
 * @Description NettyClientTest
 * @Author an
 * @Date 2019/4/24 下午3:46
 * @Version 1.0
 */
public class NettyClientTest {

    private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

    public static void main(String [] args) {
        NettyClient nettyClient = new NettyClient("127.0.0.1", 4002);
        nettyClient.startAsync();
        nettyClient.awaitRunning();
        logger.info("Client status {}", nettyClient.state());
        Message.Header header = new Message.Header(0x0709110B, "abd", Ping.LENGTH, 0);
        Ping ping = new Ping(new Random().nextInt(Integer.MAX_VALUE));
        ping.setHeader(header);
        nettyClient.writeMessage(ping);
    }


}
