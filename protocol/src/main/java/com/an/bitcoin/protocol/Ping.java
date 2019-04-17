package com.an.bitcoin.protocol;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * @ClassName Ping
 * @Description Ping
 * @Author An
 * @Date 2019/4/17 上午11:06
 * @Version 1.0
 */
public class Ping extends Message {

    private long nonce;

    public Ping(long nonce) {
        this.nonce = nonce;
    }

    public Ping(Header header) {
        this.header = header;
    }

    public Ping(long nonce, Header header) {
        this.nonce = nonce;
        super.header = header;
    }

    public Ping(byte [] payload) throws ProtocolException {
        Header header = new Header(payload);
        super.header = header;
        parse(payload, header.getHeaderLength());
    }

    @Override
    public void parse(byte[] payload, int offset) throws ProtocolException {
        nonce = readInt64(payload, offset);
    }

    @Override
    protected byte[] doSerialize() throws ProtocolException {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            uint64ToByteStream(nonce, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            throw new ProtocolException(e);
        }
    }

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public int getLength() {
        return 8;
    }

    @Override
    public int getChecksum() {
        return 0;
    }

    public long getNonce() {
        return nonce;
    }

    @Override
    public String toString() {
        return "Ping{" +
                "nonce=" + nonce +
                ", header=" + header +
                '}';
    }
}
