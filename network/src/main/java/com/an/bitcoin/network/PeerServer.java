package com.an.bitcoin.network;

import com.an.bitcoin.core.Message;
import com.an.bitcoin.core.MessageFactory;
import com.an.bitcoin.core.Peer;
import com.an.bitcoin.core.PeerFactory;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static com.an.bitcoin.core.Utils.HEX;

/**
 * @ClassName PeerServer
 * @Description PeerServer
 * @Author an
 * @Date 2019/4/22 上午9:53
 * @Version 1.0
 */
public class PeerServer extends AbstractExecutionThreadService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private PeerFactory peerFactory;
    private MessageFactory messageFactory;
    private int port;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Map<String, ChannelFuture> channelFutures = new HashMap<>();


    public PeerServer(PeerFactory peerFactory, MessageFactory messageFactory, int port) {
        this.peerFactory = peerFactory;
        this.messageFactory = messageFactory;
        this.port = port;
    }


    @Override
    protected void run() throws Exception {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("MessageDecoder", new MessageDecoder(messageFactory));
                            pipeline.addLast("MessageEncoder", new MessageEncoder());
                            pipeline.addLast("PeerHandler", new PeerHandler(peerFactory));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("Server start at {} port", port);
                    } else {
                        logger.info("Server start failed");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void connect(String hostname, String ip, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("MessageDecoder", new MessageDecoder(messageFactory));
                        pipeline.addLast("MessageEncoder", new MessageEncoder());
                        pipeline.addLast("PeerHandler", new PeerHandler(peerFactory));
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.info("Connect to {} port {}", ip, port);
                } else {
                    logger.error("Connect to {} port {} error", ip, port);
                }
            }
        });
        channelFutures.put(hostname, channelFuture);
    }

    public void disConnect(String ip) {
        ChannelFuture channelFuture = channelFutures.get(ip);
        logger.info("{} {} ", channelFuture.channel().isOpen(), channelFuture.channel().isActive());
        channelFuture.channel().disconnect();
        logger.info("{} {} ", channelFuture.channel().isOpen(), channelFuture.channel().isActive());

    }

    public void send(String hostname, String msg) {
        ChannelFuture channelFuture = channelFutures.get(hostname);
        if (channelFuture == null) {
            logger.error("Hostname {} mapping channel not found", hostname);
            return;
        }
        Channel channel = channelFutures.get(hostname).channel();
        PeerHandler peerHandler = (PeerHandler) channel.pipeline().get("PeerHandler");
        ByteArrayInputStream stream = new ByteArrayInputStream(HEX.decode(msg));
        byte[] headerBytes = new byte[24];
        try {
            stream.read(headerBytes);
            Message.Header header = new Message.Header(headerBytes, true);
            header.magic = Message.magic;
            byte[] bodyBytes = new byte[stream.available()];
            stream.read(bodyBytes);
            Message message = messageFactory.create(header, bodyBytes);
            message.setHeader(header);
            peerHandler.writeMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
