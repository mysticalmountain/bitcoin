package com.an.bitcoin.network;

import com.an.bitcoin.protocol.Address;
import com.an.bitcoin.protocol.Message;
import com.an.bitcoin.protocol.Ping;
import com.an.bitcoin.protocol.Version;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        out.println("channel active");
        while (true) {
//        for (int i = 0; i < 3; i++ ) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            byte b
            Random random  = new Random();
            Ping ping = new Ping(random.nextInt(Integer.MAX_VALUE));
//            Version version = new Version(1, 100, System.currentTimeMillis());
            Message.Header header = new Message.Header(0x0709110B, "ping", ping.getLength(), 0);
            ping.setHeader(header);
            System.out.println("ping:" + ping);
            ctx.writeAndFlush(ping);


        }
    }


//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        out.println(String.format("receive message %s", msg));
//        String str = "client reply message " + new Date();
//        ctx.writeAndFlush(str);
//
//        Thread.currentThread().sleep(5 * 1000);
//
//
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

    }
}
