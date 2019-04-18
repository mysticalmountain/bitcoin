package com.an.bitcoin.protocol;

/**
 * @ClassName MessageDecorator
 * @Description MessageDecorator
 * @Author an
 * @Date 2019/4/18 下午3:28
 * @Version 1.0
 */
public abstract class MessageDecorator<T extends Message> extends Message {

    protected T message;

    public MessageDecorator(T message) {
        this.message = message;
    }

    public MessageDecorator(byte[] payload) {
        super(payload);
    }
}
