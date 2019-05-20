package com.an.bitcoin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @ClassName Version
 * @Description Version
 * @Author an
 * @Date 2019/4/16 下午11:51
 * @Version 1.0
 */
@Command(name = "version")
public class Version extends Message {

    private int version;
    private long service;
    private long timestamp;
    private Address addrRecv;
    public static final int LENGTH = 20;

    public Version() {}

    public Version(int version, long service, long timestamp) {
        this.version = version;
        this.service = service;
        this.timestamp = timestamp;
    }

    public Version(byte[] payload) throws ProtocolException {
        super(payload);
    }

    public Version(byte[] payload, int offset) throws ProtocolException {
        super(payload);
        parse();
    }


    @Override
    public void parse() throws ProtocolException {
        this.version = (int) readUint32();
        this.service = readInt64();
        this.timestamp = readInt64();
        addrRecv = new Address(readBytes(Address.LENGTH));
    }

    @Override
    public byte[] doSerialize() throws ProtocolException {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            uint32ToByteStream(version, stream);
            uint64ToByteStream(service, stream);
            uint64ToByteStream(timestamp, stream);
            stream.write(addrRecv.serialize());
            return stream.toByteArray();
        } catch (Exception e) {
            throw new ProtocolException(e);
        }
    }

    @Override
    public String getCommand() {
        return "version";
    }

    @Override
    public int getLength() {
        if (addrRecv == null) {
            throw new ProtocolException("Message serialize error sub message addrRecv is null");
        }
        return LENGTH + addrRecv.getLength();

    }

    @Override
    public int getChecksum() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean support(Header header) {
        return getCommand().equals(header.command);
    }

    @Override
    public String toString() {
        return "Version{" +
                "version=" + version +
                ", service=" + service +
                ", timestamp=" + timestamp +
                ", addrRecv=" + addrRecv +
                ", header=" + header +
                '}';
    }

    public int getVersion() {
        return version;
    }

    public long getService() {
        return service;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Address getAddrRecv() {
        return addrRecv;
    }

    public void setAddrRecv(Address addrRecv) {
        this.addrRecv = addrRecv;
    }
}
