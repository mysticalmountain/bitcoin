package com.an.bitcoin.protocol;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * @ClassName MessageFactory
 * @Description MessageFactory
 * @Author an
 * @Date 2019/4/24 上午9:49
 * @Version 1.0
 */
public class MessageFactory {

    private static Logger logger = LoggerFactory.getLogger(MessageFactory.class);

    static Reflections reflections = new Reflections("com.an.bitcoin.protocol");
    static Set<Class<? extends Message>> subTypes = reflections.getSubTypesOf(Message.class);

    public static Message create(Message.Header header, byte[] payload) throws ProtocolException {
        for (Class<? extends Message> messageClass : subTypes) {
            Command command = messageClass.getAnnotation(Command.class);
            if (command != null && command.name().equals(header.command)) {
                try {
                    Constructor<? extends Message> constructor = messageClass.getConstructor(byte[].class);
                    Message message = constructor.newInstance(payload);
                    message.setHeader(header);
                    if (message.support(header)) {
                        return message;
                    }
                } catch (Exception e) {
                    logger.error("Unknown exception {}", e);
                }
            }
        }
        throw new ProtocolException(String.format("Message factory create message error, command %s mapping message not found", header.command));
    }

}
