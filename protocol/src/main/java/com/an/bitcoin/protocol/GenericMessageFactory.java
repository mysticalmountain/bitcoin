package com.an.bitcoin.protocol;

import com.an.bitcoin.core.Message;
import com.an.bitcoin.core.MessageFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * @ClassName GenericMessageFactory
 * @Description GenericMessageFactory
 * @Author an
 * @Date 2019/4/24 上午9:49
 * @Version 1.0
 */
public class GenericMessageFactory implements MessageFactory {

    private static Logger logger = LoggerFactory.getLogger(GenericMessageFactory.class);

    static Reflections reflections = new Reflections("com.an.bitcoin.protocol");
    static Set<Class<? extends Message>> subTypes = reflections.getSubTypesOf(Message.class);

    public Message create(Message.Header header, byte[] payload) throws ProtocolException {
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
