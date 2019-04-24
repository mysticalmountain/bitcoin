package com.an.bitcoin.network;

/**
 * @ClassName PeerException
 * @Description PeerException
 * @Author an
 * @Date 2019/4/24 下午11:07
 * @Version 1.0
 */
public class PeerException extends RuntimeException {

    public PeerException() {
    }

    public PeerException(String message) {
        super(message);
    }

    public PeerException(Throwable cause) {
        super(cause);
    }

    public PeerException(String message, Throwable cause) {
        super(message, cause);
    }

}
