package com.an.bitcoin.protocol;

import com.an.bitcoin.core.Sha256Hash;

/**
 * @ClassName LevelStore
 * @Description LevelStore
 * @Author an
 * @Date 2019/5/29 上午11:24
 * @Version 1.0
 */
public interface BlockStore {

    void put(Block block) throws BlockStoreException;

    Block get(Sha256Hash hash) throws BlockStoreException;

    Block getChainHead() throws BlockStoreException;

    void setChainHead(Block block) throws BlockStoreException;

    void close() throws BlockStoreException;
}
