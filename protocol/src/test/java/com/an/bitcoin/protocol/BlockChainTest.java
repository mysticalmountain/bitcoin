package com.an.bitcoin.protocol;

import com.an.bitcoin.core.Sha256Hash;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.an.bitcoin.protocol.Block.BLOCK_VERSION_GENESIS;
import static com.an.bitcoin.protocol.Block.EASIEST_DIFFICULTY_TARGET;
import static com.an.bitcoin.core.Utils.HEX;
import static java.lang.System.out;

/**
 * @ClassName BlockChainTest
 * @Description BlockChainTest
 * @Author an
 * @Date 2019/5/29 下午4:34
 * @Version 1.0
 */
public class BlockChainTest {

    private BlockChain blockChain = new FullBlockChain(new LevelDBBlockStore(new File("/Users/andongxu/workspace/bitcoin/db")));

    @Test
    public void addBlock() {
        Block block = getGenesisBlock();
        blockChain.add(block);
    }



    Block getGenesisBlock() {
        Block genesis = new Block();
//        genesis.setVersion((int) BLOCK_VERSION_GENESIS);
//        genesis.setPrevBlockHash(Sha256Hash.ZERO_HASH);
        genesis.setMerkleRootHash(Sha256Hash.ZERO_HASH);
//        genesis.setDifficultyTarget((int) EASIEST_DIFFICULTY_TARGET);
//        genesis.setTime((int) (System.currentTimeMillis() / 1000));
//        out.println(HEX.encode(genesis.serialize()));
        String hexStr = "0100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000df4bee5cffff7f200000000000";
        return new Block(HEX.decode(hexStr));
    }
}
