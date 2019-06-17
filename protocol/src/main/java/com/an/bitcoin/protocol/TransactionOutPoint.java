package com.an.bitcoin.protocol;

import com.an.bitcoin.core.ChildMessage;
import com.an.bitcoin.core.Sha256Hash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName TransactionOutPoint
 * @Description TransactionOutPoint
 * @Author an
 * @Date 2019/5/8 上午9:27
 * @Version 1.0
 */
public class TransactionOutPoint extends ChildMessage {

    private Sha256Hash hash;
    private int index;
    public static final int LENGTH = 32 + 4;

    public TransactionOutPoint() {
    }

    /**
     * @param hash  coin base set hash is null
     * @param index
     */
    public TransactionOutPoint(Sha256Hash hash, int index) {
        this.index = index;
        this.hash = hash == null ? Sha256Hash.ZERO_HASH : hash;
    }

    public TransactionOutPoint(byte[] payload) {
        super(payload);
    }

    @Override
    public void parse() throws ProtocolException {
        hash = Sha256Hash.wrapReversed(readBytes(32));
        index = (int) readUint32();
    }

    @Override
    protected byte[] doSerialize() throws IOException, ProtocolException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(hash.getReversedBytes());
        uint32ToByteStream(index, stream);
        return stream.toByteArray();
    }

    @Override
    public int getLength() {
        return LENGTH;
    }

    //    @Override
    public Sha256Hash getHash() {
        return hash;
    }

    public void setHash(Sha256Hash hash) {
        this.hash = hash;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
