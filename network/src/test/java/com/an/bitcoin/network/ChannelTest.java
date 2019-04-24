package com.an.bitcoin.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

import static java.lang.System.out;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @ClassName ChannelTest
 * @Description ChannelTest
 * @Author an
 * @Date 2019/4/19 上午11:52
 * @Version 1.0
 */
public class ChannelTest {

    class ServerHandler implements ChannelInboundHandler {


        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            out.println("channelRegistered");
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            out.println("channelUnregistered");

        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            out.println("channelActive");
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            out.println("channelInactive");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            out.println("channelRead");
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            out.println("channelReadComplete");
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            out.println("userEventTriggered");
        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
            out.println("channelWritabilityChanged");
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            out.println("handlerAdded");
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            out.println("handlerRemoved");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            out.println("exceptionCaught");
        }
    }

    @Test
    public void test() {
        EmbeddedChannel channel = new EmbeddedChannel(new ServerHandler());
        assertTrue(channel.isRegistered());
        channel.writeInbound(1);
//        channel.writeInbound(2);
//        byte [] data = new byte[5];
//        channel.writeInbound(data);
//        channel.readInbound();
        channel.finish();
    }
}
