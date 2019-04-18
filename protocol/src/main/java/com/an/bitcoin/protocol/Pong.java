package com.an.bitcoin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName Pong
 * @Description Pong
 * @Author an
 * @Date 2019/4/18 下午9:19
 * @Version 1.0
 */
public class Pong extends Message {

    private long nonce;
    public static final int LENGTH = 8;

    public Pong(long nonce) {
        this.nonce = nonce;
    }

    public Pong(byte [] payload) {
        super(payload);
        parse();
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

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }
}
