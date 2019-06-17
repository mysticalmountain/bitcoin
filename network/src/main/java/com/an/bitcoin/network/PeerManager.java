package com.an.bitcoin.network;

import io.netty.channel.ChannelFuture;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PeerManager
 * @Description PeerManager
 * @Author an
 * @Date 2019/6/14 下午4:33
 * @Version 1.0
 */
public class PeerManager {

    private Map<String, ChannelFuture> channelFutures = new HashMap<>();

    public void put(String ip, ChannelFuture channelFuture) {
        channelFutures.put(ip, channelFuture);
    }
}
