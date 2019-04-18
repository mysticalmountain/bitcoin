package com.an.bitcoin.protocol;

import java.io.IOException;

/**
 * @ClassName Verack
 * @Description Verack
 * @Author an
 * @Date 2019/4/18 下午9:44
 * @Version 1.0
 */
public class Verack extends Message {
    @Override
    public void parse() throws ProtocolException {

    }

    @Override
    protected byte[] doSerialize() throws IOException, ProtocolException {
        return new byte[0];
    }

    @Override
    public String getCommand() {
        return "verack";
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getChecksum() {
        return 0;
    }
}
