package com.an.bitcoin.protocol;

import com.an.bitcoin.core.ChildMessage;
import com.an.bitcoin.core.VarInt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName TransactionOutput
 * @Description TransactionOutput
 * @Author an
 * @Date 2019/5/8 上午9:32
 * @Version 1.0
 */
public class TransactionOutput extends ChildMessage {

    private long value;
    private long scriptLength;
    private byte[] scriptBytes;
    private Script script;

    public TransactionOutput() {
    }

    public TransactionOutput(byte[] payload, int cursor) {
        super(payload, cursor);
    }

    @Override
    public void parse() throws ProtocolException {
        value = readInt64();
        scriptLength = readVarInt();
        scriptBytes = readBytes((int) scriptLength);
    }

    @Override
    protected byte[] doSerialize() throws IOException, ProtocolException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        uint64ToByteStream(value, stream);
        stream.write(new VarInt(scriptLength).encode());
        stream.write(scriptBytes);
        return stream.toByteArray();
    }

    @Override
    public int getLength() {
        return 8
                + new VarInt(scriptLength).getOriginalSizeInBytes()
                + scriptBytes.length;
    }

    public Script getPkScript() {
        if (script != null) {
            return script;
        }
        script = new Script(scriptBytes);
        return script;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getScriptLength() {
        return scriptLength;
    }

    public void setScriptLength(long scriptLength) {
        this.scriptLength = scriptLength;
    }

    public byte[] getScriptBytes() {
        return scriptBytes;
    }

    public void setScriptBytes(byte[] scriptBytes) {
        this.scriptBytes = scriptBytes;
    }
}
