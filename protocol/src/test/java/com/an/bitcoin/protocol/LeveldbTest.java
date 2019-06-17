package com.an.bitcoin.protocol;

import org.iq80.leveldb.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static java.lang.System.out;
import static org.fusesource.leveldbjni.JniDBFactory.*;

import java.io.*;

/**
 * @ClassName LeveldbTest
 * @Description LeveldbTest
 * @Author an
 * @Date 2019/5/24 下午10:58
 * @Version 1.0
 */
public class LeveldbTest {

    private DB db = null;

    @Before
    public void init() throws IOException {
        Options options = new Options();
        options.createIfMissing(true);
        db = factory.open(new File("example"), options);
    }


    @Test
    public void put() throws IOException {
        try {
            db.put(bytes("k1"), bytes("v1"));
            String val = asString(db.get(bytes("k1")));
            Assert.assertEquals("v1", val);
        } finally {
            db.close();
        }
    }

    @Test
    public void delete() throws IOException {
        try {
            db.put(bytes("k1"), bytes("v1"));
            String val = asString(db.get(bytes("k1")));
            Assert.assertEquals("v1", val);
            db.delete(bytes("k1"));
            val = asString(db.get(bytes("k1")));
            Assert.assertNull(val);
        } finally {
            db.close();
        }
    }

    @Test
    public void batch() throws IOException {
        try {
            WriteBatch batch = db.createWriteBatch();
            batch.put(bytes("k1"), bytes("v1"));
            batch.put(bytes("k2"), bytes("v2"));
            batch.put(bytes("k3"), bytes("v3"));
            db.write(batch);
        } finally {
            db.close();
        }
    }

    @Test
    public void iterator() throws IOException {
        try {
            DBIterator iterator = db.iterator();
            for(iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                String key = asString(iterator.peekNext().getKey());
                String value = asString(iterator.peekNext().getValue());
                System.out.println(key+" = "+value);
            }
        } finally {
            db.close();
        }
    }

    @Test
    public void snapshot() {
        ReadOptions readOptions = new ReadOptions();
        readOptions.snapshot(db.getSnapshot());
        db.iterator(readOptions);

        db.put(bytes("k1"), bytes("V1"));
        Assert.assertEquals("V1", asString(db.get(bytes("k1"))));
        Assert.assertEquals("v1", asString(db.get(bytes("k1"), readOptions)));
    }
}
