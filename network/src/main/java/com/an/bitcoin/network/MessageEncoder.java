package com.an.bitcoin.network;

import com.an.bitcoin.core.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.util.Random;

/**
 * @ClassName MessageEncoder
 * @Description MessageEncoder
 * @Author an
 * @Date 2019/4/24 上午11:25
 * @Version 1.0
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(msg.getHeader().serialize());
        stream.write(msg.serialize());
        out.writeBytes(stream.toByteArray());
    }
}
