package com.an.bitcoin.protocol;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Random;

import static java.lang.System.out;

/**
 * @ClassName PingTest
 * @Description PingTest
 * @Author an
 * @Date 2019/4/17 上午11:42
 * @Version 1.0
 */
public class PingTest {

    @Test
    public void serialize() throws ProtocolException {
        Ping ping = new Ping(new Random().nextInt());
        Message.Header header = new Message.Header(0x0709110B, ping.getCommand(), ping.getLength(), ping.getChecksum());
        ping.setHeader(header);
        ping.serialize();
    }

    @Test
    public void deSerialize() {
        Ping ping = new Ping(100);
        Message.Header header = new Message.Header(0x0709110B, ping.getCommand(), ping.getLength(), ping.getChecksum());
        ping.setHeader(header);
        ByteBuffer buffer = ByteBuffer.wrap(ping.serialize());
        Ping ping1 = new Ping(buffer);
        out.println(ping + "\r\n" + ping1);
        Assert.assertEquals(ping.getNonce(), ping1.getNonce());
    }
}
