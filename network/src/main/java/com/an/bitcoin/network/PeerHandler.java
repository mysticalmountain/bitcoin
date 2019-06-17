package com.an.bitcoin.network;

import com.an.bitcoin.core.Message;
import com.an.bitcoin.core.MessageWrite;
import com.an.bitcoin.core.Peer;
import com.an.bitcoin.core.PeerFactory;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @ClassName PeerHandler
 * @Description PeerHandler
 * @Author an
 * @Date 2019/4/22 上午11:45
 * @Version 1.0
 */

public class PeerHandler extends SimpleChannelInboundHandler<Message> implements MessageWrite {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ChannelHandlerContext ctx;
    private PeerFactory peerFactory;
    private Peer peer;
    private SettableFuture<Void> socketChannelActiveFuture = SettableFuture.create();


    public PeerHandler(PeerFactory peerFactory) {
        this.peerFactory = peerFactory;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        logger.info("Receive message {}", msg);
        peer.receiveMessage(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        logger.info("Socket channel active address {} port {}", socketAddress.getHostString(), socketAddress.getPort());
        peer = peerFactory.get(socketAddress.getAddress(), socketAddress.getPort());
        peer.setMessageWriteTarget(this);
        socketChannelActiveFuture.set(null);
        //Add connection closed listener, if the event of a netty client to server disconnection occur, netty event notify the peer
        ChannelFuture channelFuture = ctx.channel().closeFuture();
        channelFuture.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                peer.connectionClosed();
            }
        });
        //Establish the connection of peer to peer
        peer.connectionOpened();
    }

    @Override
    public void writeMessage(Message message) {
        try {
            socketChannelActiveFuture.get();
        } catch (Exception e) {
            logger.error("Unknown exception", e);
        }
        logger.info("Write message {}", message);
        ctx.writeAndFlush(message);
    }

}
