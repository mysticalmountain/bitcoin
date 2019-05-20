package com.an.bitcoin.protocol;

import com.google.common.base.VerifyException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName Transaction
 * @Description Transaction
 * @Author an
 * @Date 2019/5/8 上午9:13
 * @Version 1.0
 */
public class Transaction extends ChildMessage {

    private int version;
    private int flag;
    private int txInCount;
    private List<TransactionInput> inputs;
    private int txOutCount;
    private List<TransactionOutput> outputs;
    private int lockTime;

    public Transaction() {
        version = 1;
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    public Transaction(byte[] payload) {
        this(payload, 0);
    }

    public Transaction(byte[] payload, int cursor) {
        super(payload, cursor);
    }

    @Override
    public void parse() throws ProtocolException {
        version = (int) readUint32();
        //TODO flag field process later
        txInCount = (int) readVarInt();
        inputs = new ArrayList<>();
        for (int i = 0; i < txInCount; i++) {
            TransactionInput input = new TransactionInput(payload, cursor);
            inputs.add(input);
            cursor += input.getLength();
        }
        txOutCount = (int) readVarInt();
        outputs = new ArrayList<>();
        for (int i = 0; i < txOutCount; i++) {
            TransactionOutput output = new TransactionOutput(payload, cursor);
            outputs.add(output);
            cursor += output.getLength();
        }
        //TODO tx_witnesses process later
        lockTime = (int) readUint32();
    }

    @Override
    protected byte[] doSerialize() throws IOException, ProtocolException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        uint32ToByteStream(version, stream);
        //TODO flag field process later
        stream.write(new VarInt(txInCount).encode());
        for (TransactionInput input : inputs) {
            stream.write(input.serialize());
        }
        stream.write(new VarInt(txOutCount).encode());
        for (TransactionOutput output : outputs) {
            stream.write(output.serialize());
        }
        //TODO tx_witnesses process later
        uint32ToByteStream(lockTime, stream);
        return stream.toByteArray();
    }

    @Override
    public int getLength() {
        int length = LENGTH_4  //version
                + new VarInt(txInCount).getOriginalSizeInBytes() //txInCoun
                + new VarInt(txOutCount).getOriginalSizeInBytes() //txOutCoun
                + LENGTH_4;    //lockTime
        for (TransactionInput input : inputs) {
            length += input.getLength();
        }
        for (TransactionOutput output : outputs) {
            length += output.getLength();
        }
        return length;
    }

    public void verify() throws VerifyException {
        //inputs and outputs must has elements
        if (inputs.size() == 0 || outputs.size() == 0)
            throw new VerificationException.EmptyInputsOrOutputs();
        Set<TransactionOutPoint> outPoints = new HashSet<>();
        //prevent double spend
        for (TransactionInput input : inputs) {
            if (outPoints.contains(input.getTransactionOutPoint()))
                throw new VerificationException.DuplicatedOutPoint();
            outPoints.add(input.getTransactionOutPoint());
        }
        //TODO validator cion
    }

    public Sha256Hash getHash() {
        return Sha256Hash.wrapReversed(Sha256Hash.hashTwice(serialize()));
    }

    public boolean isCoinBase() {
        return inputs.size() == 1 && inputs.get(0).isCoinBase();
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getTxInCount() {
        return txInCount;
    }

    public void setTxInCount(int txInCount) {
        this.txInCount = txInCount;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
    }

    public int getTxOutCount() {
        return txOutCount;
    }

    public void setTxOutCount(int txOutCount) {
        this.txOutCount = txOutCount;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public int getLockTime() {
        return lockTime;
    }

    public void setLockTime(int lockTime) {
        this.lockTime = lockTime;
    }
}
