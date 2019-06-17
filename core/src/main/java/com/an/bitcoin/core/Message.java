package com.an.bitcoin.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName Message
 * @Description Message
 * @Author An
 * @Date 2019/4/16 下午11:29
 * @Version 1.0
 */
public abstract class Message {

    public static final int LENGTH_4 = 4;
    public static final int LENGTH_8 = 8;
    public static final int LENGTH_12 = 12;
    public static final int LENGTH_16 = 16;
    public static final String ASCII = "US-ASCII";
    public static final int magic = 0x0709110B;

    protected Header header;
    protected byte [] payload;
    public static final int MAX_SIZE = 0x02000000; // 32MB
    protected int cursor;

    public Message() {

    }

    public Message(byte [] payload) {
        this(payload, 0);
    }

    public Message(byte [] payload, int cursor) {
        this.payload = payload;
        this.cursor = cursor;
        messages.add(this.getClass());
        parse();
    }

    private List<Class> messages = new LinkedList<>();


    public abstract void parse() throws ProtocolException;

//    public abstract void parse(byte[] payload) throws ProtocolException;

    protected abstract byte[] doSerialize() throws IOException, ProtocolException;

    public abstract String getCommand();

    public abstract int getLength();

    public abstract int getChecksum();

    public abstract boolean support(Header header);

    public byte[] serialize() throws ProtocolException {

        if (this instanceof ChildMessage) {
            try {
                return doSerialize();
            } catch (IOException e) {
                throw new ProtocolException(e);
            }
        }
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(doSerialize());
            if (stream.size() != getLength()) {
                throw new ProtocolException(String.format("Message serialize error expect length %d actual length %d", getLength(), stream.size()));
            }
            return stream.toByteArray();
        } catch (ProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw new ProtocolException("Message serialize error unknown", e);
        }
    }

    public static class Header {
        public int magic;
        public String command;
        public int length;
        public int checksum;
        public final int headerLength = 24;

        /**
         *
         * @param magic
         * @param command
         * @param length
         * @param checksum
         */
        public Header(int magic, String command, int length, int checksum) {
            this.magic = magic;
            this.command = command;
            this.length = length;
            this.checksum = checksum;
        }

        public Header(byte [] payload) throws ProtocolException {
            try {
//                seekPastMagicBytes(buffer);
//                magic = (int) readUint32(payload, 0);
//                ByteArrayInputStream stream = new ByteArrayInputStream(payload);
                byte [] commandBytes =  new byte [12];
//                stream.read(commandBytes);
                System.arraycopy(payload, 0, commandBytes, 0, 12);
                command = new String(commandBytes, "US-ASCII").trim();
                length = (int) readUint32(payload, 12);
                checksum = (int) readUint32(payload, 16);
            } catch (Exception e) {
                throw new ProtocolException(e);
            }
        }

        public Header(byte [] payload, boolean hasMagic) throws ProtocolException {
            try {
                magic = (int) readUint32(payload, 0);
                byte [] commandBytes =  new byte [12];
                System.arraycopy(payload, 4, commandBytes, 0, 12);
                command = new String(commandBytes, "US-ASCII").trim();
                length = (int) readUint32(payload, 12 + 4);
                checksum = (int) readUint32(payload, 16 + 4);
            } catch (Exception e) {
                throw new ProtocolException(e);
            }
        }

        public byte [] serialize() throws ProtocolException {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                uint32ToByteStream(magic, stream);
                byte [] commandBytes = new byte[12];
                System.arraycopy(command.getBytes(), 0, commandBytes, 0, command.getBytes().length);
                stream.write(commandBytes);
                uint32ToByteStream(length, stream);
                uint32ToByteStream(checksum, stream);
                byte[] res = stream.toByteArray();
                if (res.length != headerLength) {
                    throw new ProtocolException(String.format("Message header serialize error expect length %d actual length %d", headerLength, res.length));
                }
                return stream.toByteArray();
            } catch (Exception e) {
                throw new ProtocolException(e);
            }
        }

        public void seekPastMagicBytes(ByteBuffer in) throws BufferUnderflowException {
            int times = 1;
            while (times <= 3) {
                int b = in.get();
                int tb = 0xFF & (magic >> (times * 8)) ;
                System.out.println(b + "\t" + tb);
                if (b == tb) {
                    times++;
                }else {
                    times = 1;
                }
            }

        }

        public int getHeaderLength() {
            return headerLength;
        }

        public int getLength() {
            return length;
        }

        @Override
        public String toString() {
            return "Header{" +
                    "magic=" + magic +
                    ", command='" + command + '\'' +
                    ", length=" + length +
                    ", checksum=" + checksum +
                    '}';
        }
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public static long readUint32(byte[] bytes, int offset) {
        return (bytes[offset] & 0xffl) |
                ((bytes[offset + 1] & 0xffl) << 8) |
                ((bytes[offset + 2] & 0xffl) << 16) |
                ((bytes[offset + 3] & 0xffl) << 24);
    }

    protected static long readInt64(byte[] bytes, int offset) {
        return (bytes[offset] & 0xffl) |
                ((bytes[offset + 1] & 0xffl) << 8) |
                ((bytes[offset + 2] & 0xffl) << 16) |
                ((bytes[offset + 3] & 0xffl) << 24) |
                ((bytes[offset + 4] & 0xffl) << 32) |
                ((bytes[offset + 5] & 0xffl) << 40) |
                ((bytes[offset + 6] & 0xffl) << 48) |
                ((bytes[offset + 7] & 0xffl) << 56);
    }

    public static void uint32ToByteStream(int value, OutputStream stream) throws IOException {
        stream.write(0xFF & value);
        stream.write(0xFF & (value >> 8));
        stream.write(0xFF & (value >> 16));
        stream.write(0xFF & (value >> 24));
    }

    public static void uint64ToByteStream(long value, OutputStream stream) throws IOException {
        stream.write((int) (0xFF & value));
        stream.write((int) (0xFF & (value >> 8)));
        stream.write((int) (0xFF & (value >> 16)));
        stream.write((int) (0xFF & (value >> 24)));
        stream.write((int) (0xFF & (value >> 32)));
        stream.write((int) (0xFF & (value >> 40)));
        stream.write((int) (0xFF & (value >> 48)));
        stream.write((int) (0xFF & (value >> 56)));
    }

    protected byte[] readBytes(int length) throws ProtocolException {
        if (length > MAX_SIZE) {
            throw new ProtocolException("Claimed value length too large: " + length);
        }
        try {
            byte[] b = new byte[length];
            System.arraycopy(payload, cursor, b, 0, length);
            cursor += length;
            return b;
        } catch (IndexOutOfBoundsException e) {
            throw new ProtocolException(e);
        }
    }

    protected long readUint32() throws ProtocolException {
        try {
            long u = readUint32(payload, cursor);
            cursor += 4;
            return u;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ProtocolException(e);
        }
    }

    protected long readInt64() throws ProtocolException {
        try {
            long u = readInt64(payload, cursor);
            cursor += 8;
            return u;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ProtocolException(e);
        }
    }

    protected long readVarInt() throws ProtocolException {
        return readVarInt(0);
    }

    protected long readVarInt(int offset) throws ProtocolException {
        try {
            VarInt varint = new VarInt(payload, cursor + offset);
            cursor += offset + varint.getOriginalSizeInBytes();
            return varint.value;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ProtocolException(e);
        }
    }

}
