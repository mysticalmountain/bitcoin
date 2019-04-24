package com.an.bitcoin.network;

import com.an.bitcoin.protocol.Message;
import com.an.bitcoin.protocol.MessageFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @ClassName MessageDecoder
 * @Description MessageDecoder
 * @Author an
 * @Date 2019/4/24 上午11:25
 * @Version 1.0
 */
public class MessageDecoder extends ByteToMessageDecoder {

    private int cacheTimes = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        seekPastMagicBytes(in);

        byte[] headerBytes = new byte[20];
        if (in.readableBytes() < headerBytes.length) {
            in.resetReaderIndex();
            return;
        }
        in.readBytes(headerBytes, 0, headerBytes.length);
        Message.Header header = new Message.Header(headerBytes);
        byte[] commandBytes = new byte[header.getLength()];
        if (in.readableBytes() < commandBytes.length) {
            in.resetReaderIndex();
            return;
        }
        in.readBytes(commandBytes, 0, commandBytes.length);
        Message message = MessageFactory.create(header, commandBytes);
        out.add(message);
    }

    public void seekPastMagicBytes(ByteBuf in) throws Exception {
        int times = 1;
        times = cacheTimes == 0 ? 1 : cacheTimes;
        while (times <= 3) {
            if (in.readableBytes() > 0) {
                int b = in.readByte();
                int tb = 0xFF & (Message.magic >> (times * 8));
                if (b == tb) {
                    times++;
                } else {
                    times = 1;
                }
            } else {
                cacheTimes = times;
            }
        }
    }
}
