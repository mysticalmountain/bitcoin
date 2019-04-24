package com.an.bitcoin.network.handler;

import com.an.bitcoin.protocol.Message;
import com.an.bitcoin.protocol.ProtocolException;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName MessageHandler
 * @Description MessageHandler
 * @Author an
 * @Date 2019/4/24 下午9:50
 * @Version 1.0
 */
public interface MessageHandler<T extends Message> {

    public void handle(ChannelHandlerContext ctx, T message) throws ProtocolException;

    public boolean support(Message message);
}
