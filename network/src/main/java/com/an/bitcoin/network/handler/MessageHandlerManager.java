package com.an.bitcoin.network.handler;

import com.an.bitcoin.network.PeerException;
import com.an.bitcoin.protocol.Message;
import com.an.bitcoin.protocol.MessageFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName MessageHandlerManager
 * @Description MessageHandlerManager
 * @Author an
 * @Date 2019/4/24 下午11:10
 * @Version 1.0
 */
public class MessageHandlerManager {

    private Logger logger = LoggerFactory.getLogger(MessageFactory.class);

    private Reflections reflections = new Reflections("com.an.bitcoin.network.handler");
    private Set<Class<? extends MessageHandler>> subTypes = reflections.getSubTypesOf(MessageHandler.class);

    private Set<MessageHandler> messageHandlers = new HashSet<>();

    private static MessageHandlerManager messageHandlerManager = new MessageHandlerManager();

    private MessageHandlerManager() {
        for (Class<? extends MessageHandler> handlerClass : subTypes) {
            try {
                Constructor<? extends MessageHandler> constructor = handlerClass.getConstructor();
                MessageHandler messageHandler = constructor.newInstance();
                messageHandlers.add(messageHandler);
            } catch (Exception e) {
                logger.error("Unknown exception {}", e);
            }
        }
    }

    public static MessageHandlerManager getInstance() {
        return messageHandlerManager == null ? new MessageHandlerManager() : messageHandlerManager;
    }

    public MessageHandler getMessageHandler(Message message) {
        for (MessageHandler messageHandler : messageHandlers) {
            if (messageHandler.support(message)) {
                return messageHandler;
            }
        }
        throw new PeerException(String.format("MessageHandlerManager get Handler error, message handler not found, command %s", message.getCommand()));
    }


}
