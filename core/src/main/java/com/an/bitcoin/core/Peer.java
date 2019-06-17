package com.an.bitcoin.core;

import java.net.InetSocketAddress;

/**
 * @ClassName Peer
 * @Description Peer
 * @Author an
 * @Date 2019/6/12 下午10:01
 * @Version 1.0
 */
public interface Peer {

    void connectionOpened();

    void connectionClosed();

    void receiveMessage(Message message);

    void setMessageWriteTarget(MessageWrite messageWrite);

    void setPeerAddress(InetSocketAddress socketAddress);

}
