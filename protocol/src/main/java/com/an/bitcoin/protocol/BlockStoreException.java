package com.an.bitcoin.protocol;

/**
 * @ClassName ProtocolException
 * @Description ProtocolException
 * @Author an
 * @Date 2019/4/16 下午11:30
 * @Version 1.0
 */
public class BlockStoreException extends RuntimeException {

    public BlockStoreException() {
    }

    public BlockStoreException(String message) {
        super(message);
    }

    public BlockStoreException(Throwable cause) {
        super(cause);
    }

    public BlockStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
