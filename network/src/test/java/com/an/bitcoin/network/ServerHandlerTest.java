package com.an.bitcoin.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static java.lang.System.out;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

/**
 * @ClassName ServerHandlerTest
 * @Description ServerHandlerTest
 * @Author an
 * @Date 2019/4/19 上午9:32
 * @Version 1.0
 */
public class ServerHandlerTest {

    @Test
    public void tmp() {
        EmbeddedChannel channel = new EmbeddedChannel(new PeerHandler());
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(20);
        channel.writeInbound(byteBuf);
        channel.finish();
        out.println(channel.readInbound());
    }


    @Test
    public void testConstructWithChannelInitializer() {
        final Integer first = 1;
        final Integer second = 2;

        final ChannelHandler handler = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                ctx.fireChannelRead(first);
                ctx.fireChannelRead(second);
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(handler);
            }
        });
        ChannelPipeline pipeline = channel.pipeline();
        assertSame(handler, pipeline.firstContext().handler());
        assertTrue(channel.writeInbound(3));
        assertTrue(channel.finish());
        assertSame(first, channel.readInbound());
        assertSame(second, channel.readInbound());
        assertNull(channel.readInbound());
    }




}
