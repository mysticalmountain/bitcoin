package com.an.bitcoin.protocol;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

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

    protected Header header;
    protected byte [] payload;

    public abstract void parse(byte [] payload, int offset) throws ProtocolException;

    protected abstract byte[] doSerialize() throws IOException, ProtocolException;

    public abstract String getCommand();

    public abstract int getLength();

    public abstract int getChecksum();

    public byte[] serialize() throws ProtocolException {

        if (this instanceof ChildMessage) {
            try {
                return doSerialize();
            } catch (IOException e) {
                throw new ProtocolException(e);
            }
        }
        if (header == null) {
            throw new ProtocolException("Message serialize error header is null");
        }
        try {
            byte [] headerBytes = header.serialize();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(headerBytes);

            stream.write(doSerialize());

            byte [] res = stream.toByteArray();
            if (res.length != (getLength() + header.getHeaderLength())) {
                throw new ProtocolException(String.format("Message serialize error expect length %d actual length %d", getLength() + header.getHeaderLength(), res.length));
            }
            return stream.toByteArray();
        } catch (ProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw new ProtocolException("Message serialize error unknown", e);
        }
    }

    public Message() {}

    public Message(byte [] payload) {
        this.payload = payload;
    }

    public Message(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    static class Header {
        //4	magic	uint32_t	Magic value indicating message origin network, and used to seek to next message when stream state is unknown
        //12	command	char[12]	ASCII string identifying the packet content, NULL padded (non-NULL padding results in packet rejected)
        //4	length	uint32_t	Length of payload in number of bytes
        //4	checksum	uint32_t	First 4 bytes of sha256(sha256(payload))
        int magic;
        String command;
        int length;
        int checksum;
        final int headerLength = 24;

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
                magic = (int) readUint32(payload, 0);
                byte [] commandBytes =  new byte [12];
                System.arraycopy(payload, 4, commandBytes, 0, 12);
                command = new String(commandBytes, "US-ASCII").trim();
                length = (int) readUint32(payload, 16);
                checksum = (int) readUint32(payload, 20);
            } catch (Exception e) {
                throw new ProtocolException(e);
            }
        }

        public byte [] serialize() throws ProtocolException {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                uint32ToByteStream(magic, stream);
                byte [] commandBytes = new byte[12];
                System.arraycopy(getCommand().getBytes(), 0, commandBytes, 0, getCommand().getBytes().length);
                stream.write(commandBytes);
                uint32ToByteStream(getLength(), stream);
                uint32ToByteStream(checksum, stream);
                return stream.toByteArray();
            } catch (Exception e) {
                throw new ProtocolException(e);
            }
        }

        public int getMagic() {
            return magic;
        }

        public String getCommand() {
            return command;
        }

        public int getLength() {
            return length;
        }

        public int getChecksum() {
            return checksum;
        }

        public int getHeaderLength() {
            return headerLength;
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
}
