package com.an.bitcoin.protocol;

import com.an.bitcoin.core.Message;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName HeaderTest
 * @Description HeaderTest
 * @Author an
 * @Date 2019/4/18 上午11:44
 * @Version 1.0
 */
public class HeaderTest {

    @Test
    public void serialize() {
        Message.Header header = new Message.Header(0x0709110B, "version", Ping.LENGTH, 0);
        header.serialize();
    }

    @Test
    public void deSerialize() {
        Message.Header header = new Message.Header(0x0709110B, "version", Ping.LENGTH, 0);
        byte [] headerBytes = header.serialize();
        Message.Header header1 = new Message.Header(headerBytes);
        Assert.assertEquals(header.magic, header1.magic);
        Assert.assertEquals(header.command, header1.command);
        Assert.assertEquals(header.checksum, header1.checksum);
    }
}
