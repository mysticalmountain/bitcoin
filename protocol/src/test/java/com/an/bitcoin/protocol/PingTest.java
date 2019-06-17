package com.an.bitcoin.protocol;

import com.an.bitcoin.core.Message;
import com.an.bitcoin.core.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public void serialize() throws ProtocolException, IOException {
        Ping ping = new Ping(100);
        Message.Header header = new Message.Header(Message.magic, ping.getCommand(), ping.getLength(), 0);

        byte [] headerBytes = header.serialize();

        Message.Header header1 = new Message.Header(headerBytes, true);



        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(header.serialize());
        stream.write(ping.serialize());
        out.println(Utils.HEX.encode(stream.toByteArray()));


        Ping targetPing = new Ping(ping.serialize());

        Assert.assertEquals(ping.getNonce(), targetPing.getNonce());
        Assert.assertEquals(ping.getHeader().command, targetPing.getHeader().command);
    }

    @Test
    public void deSerialize() {
        Ping ping = new Ping(100);
        byte [] pingBytes = ping.serialize();
        Ping ping1 = new Ping(pingBytes);
        out.println(ping + "\r\n" + ping1);
        Assert.assertEquals(ping.getNonce(), ping1.getNonce());
    }
}
