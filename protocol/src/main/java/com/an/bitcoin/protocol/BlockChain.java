package com.an.bitcoin.protocol;

import com.an.bitcoin.core.Sha256Hash;

import java.util.List;

/**
 * @ClassName BlockChain
 * @Description BlockChain
 * @Author an
 * @Date 2019/5/29 上午11:41
 * @Version 1.0
 */
public abstract class BlockChain {

    private BlockStore blockStore;

    public BlockChain(BlockStore blockStore) {
        this.blockStore = blockStore;
    }


    public void add(Block block) {
        //TODO Temp code for test
        if (block.getPrevBlockHash().equals(Sha256Hash.ZERO_HASH)) {
            blockStore.put(block);
            return;
        }

        block.verifyHeader();
        //find pre block
        Block preBlock = blockStore.get(block.getHash());

        if (preBlock != null) {
            if (isVerifyTransactions()) {
                block.verifyTransactions();
            }
            blockStore.put(block);
        } else {
            Block currentBlock = blockStore.getChainHead();

        }


    }


    public List<Transaction> getTransactions(Address address) {

        return null;
    }

    public Double getBalance(Address address) {

        return null;
    }

    public List<Block> getBlocks(Address address) {

        return null;
    }

    public Block getBlock(Sha256Hash hash) {

        return null;
    }

    protected abstract boolean isVerifyTransactions();


}
