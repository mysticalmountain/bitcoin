package com.an.bitcoin.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**
 * @ClassName PeerInitializer
 * @Description PeerInitializer
 * @Author an
 * @Date 2019/4/22 上午11:43
 * @Version 1.0
 */
public class PeerInitializer extends ChannelInitializer<SocketChannel> {

    private PeerHandler peerHandler = new PeerHandler();
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("MessageDecoder", new MessageDecoder());
        pipeline.addLast("MessageEncoder", new MessageEncoder());
        pipeline.addLast("PeerHandler", peerHandler);
    }

    public PeerHandler getPeerHandler() {
        return peerHandler;
    }
}
