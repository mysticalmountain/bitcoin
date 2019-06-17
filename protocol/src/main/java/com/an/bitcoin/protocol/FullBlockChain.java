package com.an.bitcoin.protocol;

/**
 * @ClassName FullBlockChain
 * @Description FullBlockChain
 * @Author an
 * @Date 2019/5/29 下午4:39
 * @Version 1.0
 */
public class FullBlockChain extends BlockChain {

    public FullBlockChain(BlockStore blockStore) {
        super(blockStore);
    }

    @Override
    protected boolean isVerifyTransactions() {
        return false;
    }
}
