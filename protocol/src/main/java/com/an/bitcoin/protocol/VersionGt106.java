package com.an.bitcoin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @ClassName VersionGt106
 * @Description VersionGt106
 * @Author an
 * @Date 2019/4/18 上午9:09
 * @Version 1.0
 */
public class VersionGt106 extends MessageDecorator<Version> {

    public Address addrFrom;
    public long nonce;
    public static final int LENGTH = 8;

    public VersionGt106(Version message, long nonce) {
        super(message);
        this.nonce = nonce;
    }

    public VersionGt106(byte[] payload) {
        super(payload);
        parse();
    }

    @Override
    public void parse() throws ProtocolException {
        message = new Version(payload);
        cursor += message.getLength();
        addrFrom = new Address(readBytes(Address.LENGTH));
        nonce = readInt64();
    }

    @Override
    protected byte[] doSerialize() throws IOException, ProtocolException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(message.doSerialize());
        stream.write(addrFrom.serialize());
        uint64ToByteStream(nonce, stream);
        return stream.toByteArray();
    }

    @Override
    public String getCommand() {
        return message.getCommand();
    }

    @Override
    public int getLength() {
        return LENGTH + addrFrom.getLength() + message.getLength();
    }

    @Override
    public int getChecksum() {
        return 0;
    }

    public Address getAddrFrom() {
        return addrFrom;
    }

    public void setAddrFrom(Address addrFrom) {
        this.addrFrom = addrFrom;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public Version getVersion() {
        return message;
    }

    public void setVersion(Version version) {
        this.message = version;
    }

    @Override
    public String toString() {
        return "VersionGt106{" +
                "addrFrom=" + addrFrom +
                ", nonce=" + nonce +
                ", message=" + message +
                '}';
    }
}
