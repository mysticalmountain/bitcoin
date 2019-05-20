package com.an.bitcoin.network.handler;

import com.an.bitcoin.protocol.*;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName VersionHandler
 * @Description VersionHandler
 * @Author an
 * @Date 2019/4/25 上午10:40
 * @Version 1.0
 */
public class VersionHandler implements MessageHandler<Message> {
    @Override
    public void handle(ChannelHandlerContext ctx, Message message) throws ProtocolException {
//        Command command = message.getClass().getAnnotation(Command.class);
//        if (command.version() == 106) {
//
//        } else if (command.version() == 70001) {
//
//        } else {
//
//        }
        ctx.writeAndFlush(new Verack());
    }

    @Override
    public boolean support(Message message) {
        return Ping.class.getAnnotation(Command.class).name().equals(message.getCommand());
    }
}
