package com.an.bitcoin.network;

import com.an.bitcoin.network.handler.MessageHandlerManager;
import com.an.bitcoin.protocol.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName PeerHandler
 * @Description PeerHandler
 * @Author an
 * @Date 2019/4/22 上午11:45
 * @Version 1.0
 */

@ChannelHandler.Sharable
public class PeerHandler extends SimpleChannelInboundHandler<Message> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        logger.info("Receive message {}" + msg);
        MessageHandlerManager.getInstance().getMessageHandler(msg).handle(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
