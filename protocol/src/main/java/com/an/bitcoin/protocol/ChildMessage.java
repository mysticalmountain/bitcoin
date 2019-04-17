package com.an.bitcoin.protocol;

import com.sun.istack.internal.Nullable;

/**
 * @ClassName ChildMessage
 * @Description ChildMessage
 * @Author an
 * @Date 2019/4/17 下午2:53
 * @Version 1.0
 */
public abstract class ChildMessage extends Message {

    protected int offset;

    public ChildMessage() {}

    public ChildMessage(byte [] payload, int offset) {
        super(payload);
        this.offset = offset;
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


}
