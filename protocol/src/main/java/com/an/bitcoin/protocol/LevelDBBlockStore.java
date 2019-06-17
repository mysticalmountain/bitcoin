package com.an.bitcoin.protocol;

import com.an.bitcoin.core.Sha256Hash;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static org.fusesource.leveldbjni.JniDBFactory.bytes;
import static org.fusesource.leveldbjni.JniDBFactory.factory;

/**
 * @ClassName LevelDBBlockStore
 * @Description LevelDBBlockStore
 * @Author an
 * @Date 2019/5/29 下午4:10
 * @Version 1.0
 */
public class LevelDBBlockStore implements BlockStore {

    private static Logger logger = LoggerFactory.getLogger(LevelDBBlockStore.class);

    private static final byte[] CHAIN_HEAD_KEY = "chainHead".getBytes();

    private DB db;


    public LevelDBBlockStore(File blockStoreFile) {
        Options options = new Options();
        options.createIfMissing(true);
        try {
            db = factory.open(blockStoreFile, options);
        } catch (IOException e) {
            logger.error("Level DB connect open error", e);
        }
    }

    @Override
    public void put(Block block) throws BlockStoreException {
        db.put(block.getHash().getBytes(), block.serialize());
    }

    @Override
    public Block get(Sha256Hash hash) throws BlockStoreException {
        byte[] bytes = db.get(hash.getBytes());
        if (bytes != null) {
            Block block = new Block(bytes);
            return block;
        }
        return null;
    }

    @Override
    public Block getChainHead() throws BlockStoreException {
        byte[] bytes = db.get(CHAIN_HEAD_KEY);
        return bytes == null ? null : new Block(bytes);
    }

    @Override
    public void setChainHead(Block block) throws BlockStoreException {
        db.put(CHAIN_HEAD_KEY, block.serialize());
    }

    @Override
    public void close() throws BlockStoreException {
        try {
            db.close();
        } catch (IOException e) {
            throw new BlockStoreException(e);
        }
    }
}
