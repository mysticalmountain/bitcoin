package com.an.bitcoin.core;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * @ClassName MessageFactory
 * @Description MessageFactory
 * @Author an
 * @Date 2019/4/24 上午9:49
 * @Version 1.0
 */
public interface MessageFactory {

    Message create(Message.Header header, byte[] payload) throws ProtocolException;

}
