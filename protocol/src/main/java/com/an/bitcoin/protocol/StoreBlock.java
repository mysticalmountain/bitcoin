package com.an.bitcoin.protocol;

import java.math.BigInteger;

/**
 * @ClassName StoreBlock
 * @Description StoreBlock
 * @Author an
 * @Date 2019/5/29 上午11:27
 * @Version 1.0
 */
public class StoreBlock {

    private Block header;
    private BigInteger difficultyTarget;
    private int height;

    public StoreBlock(Block header, BigInteger difficultyTarget, int height) {
        this.header = header;
        this.difficultyTarget = difficultyTarget;
        this.height = height;
    }
}
