package com.an.bitcoin.protocol;

import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName PongTest
 * @Description PongTest
 * @Author an
 * @Date 2019/4/18 下午9:22
 * @Version 1.0
 */
public class PongTest {

    @Test
    public void serialize() {
        Pong pong = new Pong(99);
        Assert.assertEquals(Pong.LENGTH, pong.serialize().length);
    }

    @Test
    public void deSerialize() {
        Pong pong = new Pong(99);
        Pong pong1 = new Pong(pong.serialize());
        Assert.assertEquals(pong.getNonce(), pong1.getNonce());
    }
}
