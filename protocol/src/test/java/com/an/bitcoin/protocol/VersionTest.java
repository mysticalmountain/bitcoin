package com.an.bitcoin.protocol;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

import static com.an.bitcoin.protocol.Message.*;
import static java.lang.System.out;

/**
 * @ClassName VersionTest
 * @Description VersionTest
 * @Author an
 * @Date 2019/4/16 下午11:52
 * @Version 1.0
 */
public class VersionTest {

    @Test
    public void print() {

    }

    @Test
    public void serialize() throws ProtocolException {
        Version version = new Version(1, 100, System.currentTimeMillis());
        Address address = new Address(1, 2, "3", 4);
        version.setAddrRecv(address);
        Message.Header header = new Message.Header(0x0709110B, version.getCommand(), version.getLength(), version.getChecksum());
        version.setHeader(header);

        out.println(version);
        byte[] versionBytes = version.serialize();
    }

    @Test
    public void deSerialize() throws ProtocolException {
        Version version = new Version(1, 100, System.currentTimeMillis());
        Address address = new Address(1, 2, "3", 4);
        version.setAddrRecv(address);
        Message.Header header = new Message.Header(0x0709110B, version.getCommand(), version.getLength(), version.getChecksum());
        version.setHeader(header);
        out.println(version);
        byte[] versionBytes = version.serialize();
        Version version1 = new Version(versionBytes);
        out.println(version1);
        Assert.assertEquals(version.getVersion(), version1.getVersion());
        Assert.assertEquals(version.getCommand(), version1.getCommand());
        Assert.assertEquals(version.getLength(), version1.getLength());
        Assert.assertEquals(version.getChecksum(), version1.getChecksum());
        Assert.assertEquals(version.getAddrRecv().getIp(), version1.getAddrRecv().getIp());
        Assert.assertEquals(version.getAddrRecv().getPort(), version1.getAddrRecv().getPort());
    }
}
