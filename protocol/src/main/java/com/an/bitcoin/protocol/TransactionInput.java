package com.an.bitcoin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName TransactionInput
 * @Description TransactionInput
 * @Author an
 * @Date 2019/5/8 上午9:24
 * @Version 1.0
 */
public class TransactionInput extends ChildMessage {

    private TransactionOutPoint transactionOutPoint;
    private int scriptLength;
    private byte[] scriptBytes;
    private int sequence;
    private Script script;

    public TransactionInput() {
    }

    public TransactionInput(byte[] payload, int cursor) {
        super(payload, cursor);
    }

    @Override
    public void parse() throws ProtocolException {
        transactionOutPoint = new TransactionOutPoint(readBytes(TransactionOutPoint.LENGTH));
        scriptLength = (int) readVarInt();
        scriptBytes = readBytes(scriptLength);
        sequence = (int) readUint32();
    }

    @Override
    protected byte[] doSerialize() throws IOException, ProtocolException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(transactionOutPoint.serialize());
        stream.write(new VarInt(scriptLength).encode());
        stream.write(scriptBytes);
        uint32ToByteStream(sequence, stream);
        return stream.toByteArray();
    }

    @Override
    public int getLength() {
        return transactionOutPoint.getLength()                              //transactionOutpoint
                + new VarInt(scriptLength).getOriginalSizeInBytes()         //scriptLength
                + scriptBytes.length                                        //scriptBytes
                + 4;                                                        //sequence
    }

    public Script getScriptSig() {
        if (script != null) {
            return script;
        }
        script = new Script(scriptBytes);
        return script;
    }

    public boolean isCoinBase() {
        return transactionOutPoint.getHash().equals(Sha256Hash.ZERO_HASH) && transactionOutPoint.getIndex() == 0;
    }

    public TransactionOutPoint getTransactionOutPoint() {
        return transactionOutPoint;
    }

    public void setTransactionOutPoint(TransactionOutPoint transactionOutPoint) {
        this.transactionOutPoint = transactionOutPoint;
    }

    public int getScriptLength() {
        return scriptLength;
    }

    public void setScriptLength(int scriptLength) {
        this.scriptLength = scriptLength;
    }

    public byte[] getScriptBytes() {
        return scriptBytes;
    }

    public void setScriptBytes(byte[] scriptBytes) {
        this.scriptBytes = scriptBytes;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
