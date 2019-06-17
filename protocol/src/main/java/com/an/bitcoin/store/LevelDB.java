package com.an.bitcoin.store;

import com.an.bitcoin.protocol.LevelDBBlockStore;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static org.fusesource.leveldbjni.JniDBFactory.factory;

/**
 * @ClassName LevelDB
 * @Description LevelDB
 * @Author an
 * @Date 2019/5/30 上午9:15
 * @Version 1.0
 */
public class LevelDB {

    private static Logger logger = LoggerFactory.getLogger(LevelDB.class);


    protected DB db;


    public LevelDB(File blockStoreFile) {
        Options options = new Options();
        options.createIfMissing(true);
        try {
            db = factory.open(blockStoreFile, options);
        } catch (IOException e) {
            logger.error("Level DB connect open error", e);
        }
    }
}
