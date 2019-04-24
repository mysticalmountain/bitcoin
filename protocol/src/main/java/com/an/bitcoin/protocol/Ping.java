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
@Command(name = "ping")
public class Ping extends Message {

    private long nonce;
    public static final int LENGTH = 8;


    public Ping(long nonce) {
        this.nonce = nonce;
    }

    public Ping(byte [] payload) throws ProtocolException {
        super(payload);
        parse();
    }

    @Override
    public void parse() throws ProtocolException {
        nonce = readInt64();
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

    @Override
    public boolean support(Header header) {
        return getCommand().equals(header.command);
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
