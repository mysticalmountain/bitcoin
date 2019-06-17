package com.an.bitcoin.network;

import com.an.bitcoin.core.Message;
import com.an.bitcoin.core.MessageFactory;
import com.an.bitcoin.core.PeerFactory;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;


/**
 * @ClassName PeerClient
 * @Description PeerClient
 * @Author an
 * @Date 2019/4/22 下午5:03
 * @Version 1.0
 */
public class PeerClient extends AbstractExecutionThreadService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String ip;
    private int port;
    private PeerHandler peerHandler;
    private SettableFuture<Void> startedFuture = SettableFuture.create();
    private PeerFactory peerFactory;
    private MessageFactory messageFactory;

    public PeerClient(PeerFactory peerFactory, MessageFactory messageFactory, String ip, int port) {
        this.peerFactory = peerFactory;
        this.messageFactory = messageFactory;
        peerHandler = new PeerHandler(peerFactory);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected void run() throws Exception {
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
                        pipeline.addLast("PeerHandler", peerHandler);
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    logger.info("Client started, connect to {} port {}", ip, port);
                    startedFuture.set(null);
                } else {
                    logger.error("Netty client start failed exit");
                    System.exit(-1);
                }
            }
        });
    }

    public SettableFuture<Void> getStartedFuture() {
        return startedFuture;
    }

    public void writeMessage(Message message) {
        try {
            startedFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        peerHandler.writeMessage(message);
    }
}
