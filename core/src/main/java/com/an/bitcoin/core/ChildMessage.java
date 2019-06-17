package com.an.bitcoin.core;


import javax.annotation.Nullable;

/**
 * @ClassName ChildMessage
 * @Description ChildMessage
 * @Author an
 * @Date 2019/4/17 下午2:53
 * @Version 1.0
 */
public abstract class ChildMessage extends Message {

    public ChildMessage() {}

    public ChildMessage(byte [] payload) {
        super(payload);
    }

    public ChildMessage(byte [] payload, int cursor) {
        super(payload, cursor);
    }

    @Nullable
    protected Message parent;

    @Override
    public int getChecksum() {
        return -1;
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public boolean support(Header header) {
        return false;
    }
}
