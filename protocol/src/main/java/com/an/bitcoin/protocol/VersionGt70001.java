package com.an.bitcoin.protocol;

import java.io.IOException;

@Command(name = "version")
public class VersionGt70001 extends MessageDecorator<VersionGt106> {


    private boolean relay;


    public VersionGt70001(VersionGt106 message) {
        super(message);
    }

    public VersionGt70001(byte[] payload) {
        super(payload);
    }

    @Override
    public void parse() throws ProtocolException {

    }

    @Override
    protected byte[] doSerialize() throws IOException, ProtocolException {
        return new byte[0];
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public int getChecksum() {
        return 0;
    }

    @Override
    public boolean support(Header header) {
        return getCommand().equals(header.command) && message.message.getVersion() > 70001;
    }
}
