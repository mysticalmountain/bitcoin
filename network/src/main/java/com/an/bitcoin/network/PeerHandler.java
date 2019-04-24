package com.an.bitcoin.network;

import com.an.bitcoin.network.handler.MessageHandlerManager;
import com.an.bitcoin.protocol.Message;
import com.an.bitcoin.protocol.Pong;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

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

//        Pong pong = new Pong(new Random().nextInt(Integer.MAX_VALUE));
//        Message.Header header = new Message.Header(0x0709110B, "pong", Pong.LENGTH, 0);
//        pong.setHeader(header);
//        logger.info("Write message {}", pong);
//        ctx.writeAndFlush(pong);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
