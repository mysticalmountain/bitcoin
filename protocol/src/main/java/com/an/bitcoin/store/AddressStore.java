package com.an.bitcoin.store;

import com.an.bitcoin.protocol.Address;
import com.an.bitcoin.protocol.BlockStoreException;
import com.an.bitcoin.core.Sha256Hash;
import com.an.bitcoin.protocol.Transaction;

import java.io.File;

/**
 * @ClassName AddressStore
 * @Description AddressStore
 * @Author an
 * @Date 2019/5/29 下午11:00
 * @Version 1.0
 */
public class AddressStore extends LevelDB{

    public AddressStore(File blockStoreFile) {
        super(blockStoreFile);
    }

    public void linkTransaction(Address address, Transaction ... transactions) {
//        db.get(address.)
    }
}
