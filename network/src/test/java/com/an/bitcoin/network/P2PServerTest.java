package com.an.bitcoin.network;

import com.an.bitcoin.core.*;
import com.google.common.util.concurrent.SettableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @ClassName P2PServerTest
 * @Description P2PServerTest
 * @Author an
 * @Date 2019/6/13 上午11:47
 * @Version 1.0
 */
public class P2PServerTest {

    private static Logger log = LoggerFactory.getLogger(P2PServerTest.class);

    public static void main(String[] args) throws InterruptedException {

        PeerServer server = new PeerServer(
                new PeerFactory() {
                    @Override
                    public Peer get(InetAddress address, int prot) {

                        SettableFuture<Void> pingMessageReceivedFuture = SettableFuture.create();

                        return new Peer() {
                            MessageWrite messageWrite;

                            //Attention, do not blocking this method, otherwise connection not establish
                            //
                            @Override
                            public void connectionOpened() {
                                log.info("Connection is opened");
                                //Response to peer pong message

                                log.info("Response pong message to other side");

                                Message message = new Message() {
                                    @Override
                                    public void parse() throws ProtocolException {

                                    }

                                    @Override
                                    protected byte[] doSerialize() throws IOException, ProtocolException {
                                        return new byte[30];
                                    }

                                    @Override
                                    public String getCommand() {
                                        return "ping";
                                    }

                                    @Override
                                    public int getLength() {
                                        return 30;
                                    }

                                    @Override
                                    public int getChecksum() {
                                        return 0;
                                    }

                                    @Override
                                    public boolean support(Header header) {
                                        return false;
                                    }
                                };
                                Message.Header header = new Message.Header(Message.magic, message.getCommand(), message.getLength(), 0);
                                message.setHeader(header);
                                messageWrite.writeMessage(message);

                            }

                            @Override
                            public void connectionClosed() {
                                log.info("Connection is closed");
                            }

                            @Override
                            public void receiveMessage(Message message) {
                                log.info("Receive message {}", message);
                                pingMessageReceivedFuture.set(null);
                            }

                            @Override
                            public void setMessageWriteTarget(MessageWrite messageWrite) {
                                this.messageWrite = messageWrite;
                            }
                        };
                    }
                },
                new MessageFactory() {
                    @Override
                    public Message create(Message.Header header, byte[] payload) throws ProtocolException {
                        return new Message() {
                            @Override
                            public void parse() throws ProtocolException {

                            }

                            @Override
                            protected byte[] doSerialize() throws IOException, ProtocolException {
                                return new byte[0];
                            }

                            @Override
                            public String getCommand() {
                                return "ping";
                            }

                            @Override
                            public int getLength() {
                                return 10;
                            }

                            @Override
                            public int getChecksum() {
                                return 0;
                            }

                            @Override
                            public boolean support(Header header) {
                                return false;
                            }

                            @Override
                            public String toString() {
                                return "ping";
                            }
                        };
                    }
                }, 4000);

        server.startAsync();
        server.awaitRunning();
    }
}
