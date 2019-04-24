package com.an.bitcoin.network.handler;

import com.an.bitcoin.protocol.*;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName PingHandler
 * @Description PingHandler
 * @Author an
 * @Date 2019/4/24 下午9:59
 * @Version 1.0
 */
public class PingHandler implements MessageHandler<Ping> {

    @Override
    public void handle(ChannelHandlerContext ctx, Ping message) throws ProtocolException {
        Pong pong = new Pong(message.getNonce());
        Message.Header header = message.getHeader();
        header.command = "pong";
        header.length = pong.getLength();
        header.checksum = pong.getChecksum();
        pong.setHeader(header);
        ctx.writeAndFlush(pong);
    }

    @Override
    public boolean support(Message message) {
        return Ping.class.getAnnotation(Command.class).name().equals(message.getCommand());
    }
}
