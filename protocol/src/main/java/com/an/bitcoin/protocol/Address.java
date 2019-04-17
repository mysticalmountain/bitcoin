package com.an.bitcoin.protocol;

import java.nio.ByteBuffer;

/**
 * @ClassName Address
 * @Description Address
 * @Author an
 * @Date 2019/4/17 下午2:55
 * @Version 1.0
 */
public class Address extends ChildMessage {

    private int time;
    private int services;
    private String ip;
    private int port;


    @Override
    public void parse(ByteBuffer buffer) throws ProtocolException {

    }

    @Override
    protected byte[] getBytes() throws ProtocolException {
        return new byte[0];
    }

    @Override
    public int getLength() {
        return 0;
    }


}
