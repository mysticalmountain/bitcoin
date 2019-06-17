package com.an.bitcoin.store;

import com.an.bitcoin.protocol.Block;
import com.an.bitcoin.protocol.BlockStoreException;
import com.an.bitcoin.core.Sha256Hash;

/**
 * @ClassName LevelStore
 * @Description LevelStore
 * @Author an
 * @Date 2019/5/29 下午10:49
 * @Version 1.0
 */
public abstract class LevelStore {


    abstract <T> void addSub(T t);

    abstract <T> void put(T t) throws BlockStoreException;

    abstract <T> void putSubLevel(T t) throws BlockStoreException;

//    Block get(Sha256Hash hash) throws BlockStoreException;
//
//    Block getChainHead() throws BlockStoreException;
//
//    void setChainHead(Block block) throws BlockStoreException;
//
//    void close() throws BlockStoreException;

}
