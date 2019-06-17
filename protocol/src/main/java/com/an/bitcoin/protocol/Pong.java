package com.an.bitcoin.protocol;

import com.an.bitcoin.core.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName Pong
 * @Description Pong
 * @Author an
 * @Date 2019/4/18 下午9:19
 * @Version 1.0
 */
@Command(name = "pong")
public class Pong extends Message {

    private long nonce;
    public static final int LENGTH = 8;

    public Pong(long nonce) {
        this.nonce = nonce;
    }

    public Pong(byte [] payload) {
        super(payload);
    }

    @Override
    public void parse() throws ProtocolException {
        nonce =readInt64();
    }

    @Override
    protected byte[] doSerialize() throws IOException, ProtocolException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        uint64ToByteStream(nonce, stream);
        return stream.toByteArray();
    }

    @Override
    public String getCommand() {
        return "pong";
    }

    @Override
    public int getLength() {
        return LENGTH;
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

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    @Override
    public String toString() {
        return "Pong{" +
                "nonce=" + nonce +
                ", header=" + header +
                '}';
    }
}
