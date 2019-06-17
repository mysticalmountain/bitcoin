package com.an.bitcoin.core;

import java.net.InetAddress;

/**
 * @ClassName PeerFactory
 * @Description PeerFactory
 * @Author an
 * @Date 2019/6/13 上午8:38
 * @Version 1.0
 */
public interface PeerFactory {

    Peer get(InetAddress address, int prot);

}
