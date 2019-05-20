package com.an.bitcoin.protocol;

import org.junit.Test;

import static java.lang.System.out;

/**
 * @ClassName Sha256Test
 * @Description Sha256Test
 * @Author an
 * @Date 2019/4/25 上午11:09
 * @Version 1.0
 */
public class Sha256Test {

    @Test
    public void string() {
        out.println(Sha256Hash.ZERO_HASH.toString());
    }

    @Test
    public void digital() {
        out.println(Sha256Hash.newDigest().digest(Sha256Hash.ZERO_HASH.getBytes()));
    }

    @Test
    public void hash32bytes() {
        Sha256Hash sha256Hash = Sha256Hash.wrap(new byte[32]);
        out.println(sha256Hash.toString());
    }

    @Test
    public void sha64bytes() {
        Sha256Hash sha256Hash = Sha256Hash.wrap(new byte[64]);
        out.println(sha256Hash.toString());
    }

    @Test
    public void getBytes() {
        Sha256Hash sha256Hash = Sha256Hash.wrap(new byte[32]);
        out.println(sha256Hash.getBytes().length);
        out.println(sha256Hash.toString());

    }
}
