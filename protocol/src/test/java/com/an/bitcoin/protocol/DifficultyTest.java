package com.an.bitcoin.protocol;

import com.an.bitcoin.core.Utils;
import org.junit.Test;

import java.math.BigInteger;

import static java.lang.System.out;

/**
 * @ClassName DifficultyTest
 * @Description DifficultyTest
 * @Author an
 * @Date 2019/5/15 上午11:22
 * @Version 1.0
 */
public class DifficultyTest {

    //    long difficulty = 0x20fffffL;
    long difficulty = 0x1b0404cb;
    long MIN_DIFFICULTY = 0x01007f00L;

    @Test
    public void difficultyToTarget() {
        int byteSize = (int) (difficulty >> 24 & 0xff);
        byte[] targetBytes = new byte[byteSize];
        if (byteSize >= 1) targetBytes[3] = (byte) (difficulty >> 16 & 0xff);
        if (byteSize >= 2) targetBytes[4] = (byte) (difficulty >> 8 & 0xff);
        if (byteSize >= 3) targetBytes[5] = (byte) (difficulty & 0xff);
        out.println(String.format("byte size %d", byteSize));
        BigInteger val = new BigInteger(targetBytes);
        out.println(val.toString(16));
    }

    @Test
    public void difficultyToTarget2() {
        BigInteger val = Utils.decodeCompactBits(difficulty);
        out.println(val.toString(16));
    }

    @Test
    public void calculateDifficulty() {
        BigInteger currentTarget = Utils.decodeCompactBits(difficulty);
        BigInteger largestTarget = BigInteger.valueOf(1).shiftLeft(256);
        out.println(largestTarget.toString(16));
        out.println(currentTarget.toString(16));
        out.println(largestTarget.divide(currentTarget).floatValue());
    }

    @Test
    public void tmp() {
        out.println(Utils.decodeCompactBits(0x207fFFFFL).toString(16));
        out.println(BigInteger.valueOf(0x207fFFFFL).toString(16));
        out.println(BigInteger.valueOf(1).shiftLeft(256).toString(16));

    }

}
