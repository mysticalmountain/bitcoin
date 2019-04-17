package com.an.bitcoin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @ClassName Address
 * @Description Address
 * @Author an
 * @Date 2019/4/17 下午2:55
 * @Version 1.0
 */
public class Address extends ChildMessage {

    private int time;
    private int services;
    private String ip;
    private int port;
    public static final int LENGTH = 28;

    public Address() {}

    public Address(byte[] payload, int offset) {
        super(payload, offset);
        parse(payload, offset);
    }

    public Address(int time, int services, String ip, int port) {
        this.time = time;
        this.services = services;
        this.ip = ip;
        this.port = port;
    }


    @Override
    public void parse(byte[] payload, int offset) throws ProtocolException {
        time = (int) readUint32(payload, offset);
        services = (int) readUint32(payload, offset += LENGTH_4);
        byte[] ipBytes = new byte[LENGTH_16];
        System.arraycopy(payload, offset+= LENGTH_4, ipBytes, 0, ipBytes.length);
        try {
            ip = new String(ipBytes, ASCII).trim();
        } catch (UnsupportedEncodingException e) {
            throw new ProtocolException(e);
        }
        port = (int) readUint32(payload, offset += LENGTH_16);
    }

    @Override
    protected byte[] doSerialize() throws ProtocolException, IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        uint32ToByteStream(time, stream);
        uint32ToByteStream(services, stream);
        byte[] ipBytes = new byte[LENGTH_16];
        System.arraycopy(ip.getBytes(ASCII), 0, ipBytes, 0, ip.getBytes(ASCII).length);
        stream.write(ipBytes);
        uint32ToByteStream(port, stream);
        return stream.toByteArray();
    }

    @Override
    public int getLength() {
        return LENGTH;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getServices() {
        return services;
    }

    public void setServices(int services) {
        this.services = services;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Address{" +
                "time=" + time +
                ", services=" + services +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
