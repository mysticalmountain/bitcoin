package com.an.bitcoin.protocol;

import com.google.common.io.BaseEncoding;
import org.junit.Test;
import org.spongycastle.util.BigIntegers;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;

import static java.lang.System.out;

/**
 * @ClassName HEXTest
 * @Description HEXTest
 * @Author an
 * @Date 2019/4/25 上午10:55
 * @Version 1.0
 */
public class HEXTest {

    @Test
    public void tmp() {
        BaseEncoding HEX = BaseEncoding.base16().upperCase();

        byte[] bytes = new byte[10];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (i + 10);
        }
        out.println(HEX.encode(bytes));


        int a = 0xab;
        byte[] b = new byte[4];
        b[0] = (byte) ((a >> 24) & 0xff);
        b[1] = (byte) ((a >> 16) & 0xff);
        b[2] = (byte) ((a >> 8) & 0xff);
        b[3] = (byte) (a & 0xff);
        out.println(HEX.encode(b));
        out.println(new BigInteger(b).toString(16));
//        out.println(HEX.encode());
    }
}
