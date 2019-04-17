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
public class Version extends Message {

    private int version;
    private long service;
    private long timestamp;

    public Version(int version, long service, long timestamp) {
        this.version = version;
        this.service = service;
        this.timestamp = timestamp;
    }

    public Version(ByteBuffer buffer) throws ProtocolException {
        Header header = new Header(buffer);
        super.setHeader(header);
        parse(buffer);
    }


    @Override
    public void parse(ByteBuffer buffer) throws ProtocolException {
        byte [] versionBytes = new byte[4];
        buffer.get(versionBytes);
        this.version = (int) readUint32(versionBytes, 0);
        byte [] serviceBytes = new byte[8];
        buffer.get(serviceBytes);
        this.service = readInt64(serviceBytes, 0);
        byte [] timestampBytes = new byte[8];
        buffer.get(timestampBytes);
        this.timestamp = readInt64(timestampBytes, 0);
    }

    @Override
    public byte [] getBytes() throws ProtocolException {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            uint32ToByteStream(version, stream);
            uint64ToByteStream(service, stream);
            uint64ToByteStream(timestamp, stream);
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
        return 20;
    }

    @Override
    public int getChecksum() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "Version{" +
                "version=" + version +
                ", service=" + service +
                ", timestamp=" + timestamp +
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
}
