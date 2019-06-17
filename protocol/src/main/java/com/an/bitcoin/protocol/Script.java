package com.an.bitcoin.protocol;

import com.an.bitcoin.core.ECKey;
import com.an.bitcoin.core.Sha256Hash;
import com.an.bitcoin.core.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static com.an.bitcoin.core.Utils.HEX;

/**
 * @ClassName Script
 * @Description Script
 * @Author an
 * @Date 2019/5/21 上午10:05
 * @Version 1.0
 */
public class Script {


    private List<Element> elements = new ArrayList<>();
    private Stack<byte[]> stack = new Stack<>();
    private static List<Code> codes = new ArrayList<>();
    private static List<Code> pushCodes = new ArrayList<>();
    private CodeExecutor codeExecutor = new CodeExecutor();

    public static final Code OP_PUSHDATA1 = new Code("OP_PUSHDATA1", 0x4c);
    public static final Code OP_PUSHDATA2 = new Code("OP_PUSHDATA2", 0x4d);
    public static final Code OP_PUSHDATA4 = new Code("OP_PUSHDATA4", 0x4e);
    public static final Code OP_1NEGATE = new Code("OP_1NEGATE", 0x4f);
    public static final Code OP_TRUE = new Code("OP_TRUE", 0x51);
    public static final Code OP_2To16 = new Code("OP_2To16", 0x52, 0x60);

    public static final Code OP_DUP = new Code("OP_DUP", 0x76);
    public static final Code OP_HASH160 = new Code("OP_HASH160", 0xa9);
    public static final Code OP_EQUALVERIFY = new Code("OP_EQUALVERIFY", 0x88);
    public static final Code OP_CHECKSIG = new Code("OP_CHECKSIG", 0xac);

    static {
        pushCodes.add(OP_PUSHDATA1);
        pushCodes.add(OP_PUSHDATA2);
        pushCodes.add(OP_PUSHDATA4);
        pushCodes.add(OP_1NEGATE);
        pushCodes.add(OP_TRUE);
        pushCodes.add(OP_2To16);
    }

    {
        if (codes.size() <= 0) {
            Field[] fields = Script.class.getFields();
            for (Field field : fields) {
                if (field.getType().getName().equals(Code.class.getName())) {
                    field.setAccessible(true);
                    try {
                        codes.add((Code) field.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Script(byte [] code) {
        try {
            parse(code);
        } catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    public void parse(byte[] code) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(code);
        int opCode = stream.read();
        Element element = null;
        int pushDataLength = -1;
        if (opCode >= 0 && opCode < Script.OP_PUSHDATA1.val) {
            if (stream.available() < 1) throw new ScriptException("Stream available length less 1");
            pushDataLength = opCode;
        } else if (opCode == Script.OP_PUSHDATA2.val) {
            if (stream.available() < 2) throw new ScriptException("Stream available length less 2");
            pushDataLength = stream.read() | (stream.read() << 8);
        } else if (opCode == Script.OP_PUSHDATA4.val) {
            if (stream.available() < 4) throw new ScriptException("Stream available length less 4");
            pushDataLength = stream.read() | (stream.read() << 8) | (stream.read() << 16) | (stream.read() << 24);
        }
        if (stream.available() < pushDataLength)
            throw new ScriptException("Stream available length less " + pushDataLength);
        byte[] pushData = new byte[pushDataLength];
        stream.read(pushData);
        elements.add(new Element(pushDataLength, pushData));
    }

    public Script(List<Element> elements) {
        this.elements = elements;
    }


    public Script append(Element element) {
        elements.add(element);
        return this;
    }

    public void run() {
        for (Element element : elements) {
            codeExecutor.execute(element.getCode(), element.dataBytes);
        }
    }


    static class Element {
        int code;
        byte[] dataBytes;

        public Element(int code) {
            this.code = code;
        }

        public Element(int code, byte[] dataBytes) {
            this.code = code;
            this.dataBytes = dataBytes;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public byte[] getDataBytes() {
            return dataBytes;
        }

        public void setDataBytes(byte[] dataBytes) {
            this.dataBytes = dataBytes;
        }
    }

    protected void add(Element element) {
        elements.add(element);
    }


    static class Code {
        int val;
        int[] vals;
        int begin;
        int end;
        CodeType codeType;
        String opCode;

        public Code(String opCode, int val) {
            this.opCode = opCode;
            this.codeType = CodeType.VAL;
            this.val = val;
        }

        public Code(String opCode, int... val) {
            this.opCode = opCode;
            this.codeType = CodeType.ENUM;
            this.vals = vals;
        }

        public Code(String opCode, int begin, int end) {
            this.opCode = opCode;
            this.codeType = CodeType.RANGE;
            this.begin = begin;
            this.end = end;
        }

        @Override
        public String toString() {
            String str = "";
            if (CodeType.ENUM.equals(codeType)) {
                str += "[";
                for (int i = 0; i < vals.length; i++) {
                    if (i + 1 == vals.length) {
                        str += val + "]";
                    } else {
                        str += val + ",";
                    }
                }
            } else if (CodeType.RANGE.equals(codeType)) {
                str += "[" + begin + " to " + end + "]";
            } else {
                str = "" + val;
            }
            return str;
        }
    }

    enum CodeType {
        VAL, RANGE, ENUM
    }


    class CodeExecutor {
        public void execute(int opCode, byte[] dataBytes) throws ScriptException {
            Method method = null;
            for (Code code : codes) {
                try {
                    if (code.codeType.equals(CodeType.ENUM)) {
                        if (Arrays.asList(code.vals).contains(opCode)) {
                            method = invoke(this, code, dataBytes);
                        }
                    } else if (code.codeType.equals(CodeType.RANGE)) {
                        if (opCode >= code.begin && opCode <= code.end) {
                            method = invoke(this, code, dataBytes);
                        }
                    } else if (code.codeType.equals(CodeType.VAL)) {
                        if (opCode == code.val) {
                            method = invoke(this, code, dataBytes);
                        }
                    } else {
                        throw new ScriptException(String.format("Script execute error code type %s not support", code.codeType));
                    }
                    if (method != null) {
                        return;
                    }
                } catch (InvocationTargetException e) {
                    if (e.getTargetException() instanceof ScriptException) {
                        Throwable t = e.getTargetException();
                        throw (ScriptException) t;
                    } else {
                        throw new ScriptException(e);
                    }
                } catch (NoSuchMethodException e) {
                    throw new ScriptException(String.format("Script execute error method %s not found", code.opCode.toLowerCase()));
                } catch (Exception e) {
                    throw new ScriptException(e);
                }
            }
            throw new ScriptException(String.format("Script execute error opcode %s not support", BigInteger.valueOf(opCode).toString(16)));
        }

        public Method invoke(CodeExecutor codeExecutor, Code cd, byte[] dataBytes) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method method = null;
            for (Code code : pushCodes) {
                if (code.equals(cd)) {
                    method = codeExecutor.getClass().getMethod(code.opCode.toLowerCase(), byte[].class);
                    method.invoke(codeExecutor, dataBytes);
                    return method;
                } else {
                    method = codeExecutor.getClass().getMethod(cd.opCode.toLowerCase());
                    method.invoke(codeExecutor);
                    return method;
                }
            }
            return method;
        }

        public void op_pushdata1(byte[] dataBytes) {
            stack.push(dataBytes);
        }

        public void op_dup() {
            byte[] bytes = stack.pop();
            stack.push(bytes);
            stack.push(bytes);
        }

        public void op_hash160() {
            byte[] bytes = stack.pop();
            stack.push(Utils.sha256hash160(Sha256Hash.hash(bytes)));
        }

        public void op_equalverify() {
            String str1 = HEX.encode(stack.pop());
            String str2 = HEX.encode(stack.pop());
            if (!str1.equals(str2)) {
                throw new ScriptException(String.format("Script execute op_equalverify error %s vs %s", str1, str2));
            }
        }

        public void op_checksig() throws SignatureException {
            byte[] pubkey = stack.pop();
            byte[] signature = stack.pop();
            byte[] source = stack.pop();
            ECKey ecKey = ECKey.fromPublicOnly(pubkey);
            ecKey.verifyOrThrow(source, signature);
        }
    }

    @Override
    public String toString() {
        String res = "";
        for (Element element : elements) {
            for (Code code : pushCodes) {
                if (Arrays.asList(code.vals).contains(element.code)) {
                    res += Utils.HEX.encode(element.dataBytes) + " ";
                } else if (element.code >= code.begin && element.code <= code.end) {
                    res += Utils.HEX.encode(element.dataBytes) + " ";
                } else if (element.code == code.val) {
                    res += Utils.HEX.encode(element.dataBytes) + " ";
                }
            }
            for (Code code : codes) {
                if (pushCodes.contains(code)) {
                    continue;
                }
                if (code.val == element.code) {
                    res += code.opCode + " ";
                }
            }
        }
        return res.trim();
    }
}
