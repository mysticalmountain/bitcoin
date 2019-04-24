package com.an.bitcoin.network;

import com.an.bitcoin.protocol.Message;
import org.reflections.Reflections;

import java.util.Set;

/**
 * @ClassName MessageTest
 * @Description MessageTest
 * @Author an
 * @Date 2019/4/24 上午8:53
 * @Version 1.0
 */
public class MessageTest {

    public static void main(String [] args) {
        Reflections reflections = new Reflections("com.an.bitcoin.protocol");
        Set<Class< ? extends Message>> subTypes = reflections.getSubTypesOf(Message.class);
        subTypes.forEach((message) -> {
            System.out.println(message.getName() + "\t " + message.getClass());
        });
    }
}
