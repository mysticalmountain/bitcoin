package com.an.bitcoin.core;

/**
 * @ClassName ProtocolException
 * @Description ProtocolException
 * @Author an
 * @Date 2019/4/16 下午11:30
 * @Version 1.0
 */
public class ProtocolException extends RuntimeException {

    public ProtocolException() {
    }

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(Throwable cause) {
        super(cause);
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }
}
