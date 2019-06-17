package com.an.bitcoin.protocol;

import com.an.bitcoin.core.ECKey;
import com.an.bitcoin.core.Sha256Hash;
import com.an.bitcoin.core.Utils;
import org.junit.Test;

import static java.lang.System.out;

/**
 * @ClassName ScriptTest
 * @Description ScriptTest
 * @Author an
 * @Date 2019/5/21 下午6:15
 * @Version 1.0
 */
public class ScriptTest {

    @Test
    public void run() {
        new ScriptBuilder().append(new Script.Element(Script.OP_PUSHDATA1.val, new byte[1]))
                .append(new Script.Element(Script.OP_DUP.val))
                .build()
                .run();
    }

    @Test
    public void runFail() {
        new ScriptBuilder().append(new Script.Element(Script.OP_PUSHDATA1.val, new byte[]{'A'}))
                .append(new Script.Element(Script.OP_PUSHDATA1.val, new byte[]{'B'}))
                .append(new Script.Element(Script.OP_EQUALVERIFY.val))
                .build()
                .run();
    }


    @Test
    public void toStr() {
        String str = new ScriptBuilder().append(new Script.Element(Script.OP_PUSHDATA1.val, new byte[]{'A', 'B'}))
                .append(new Script.Element(Script.OP_DUP.val))
                .append(new Script.Element(Script.OP_PUSHDATA1.val, new byte[20]))
                .append(new Script.Element(Script.OP_HASH160.val))
                .build()
                .toString();
        out.println(str);
    }

    @Test
    public void checkSig() {
        String str = new ScriptBuilder().append(new Script.Element(Script.OP_PUSHDATA1.val, new byte[]{'A', 'B'}))
                .append(new Script.Element(Script.OP_PUSHDATA1.val, new byte[]{'A', 'B'}))
                .append(new Script.Element(Script.OP_CHECKSIG.val))
                .build()
                .toString();
        out.println(str);
    }

    @Test
    public void pay2pubkey() {
        ECKey ecKey = new ECKey();
        byte[] dataBytes = new byte[]{'A', 'B'};
        ECKey.ECDSASignature signature = ecKey.sign(Sha256Hash.wrap(Sha256Hash.hash(dataBytes)));
        byte[] pub = ecKey.getPubKey();
        new ScriptBuilder()
                .append(new Script.Element(Script.OP_PUSHDATA1.val, Sha256Hash.hash(dataBytes)))
                .append(new Script.Element(Script.OP_PUSHDATA1.val, signature.encodeToDER()))
                .append(new Script.Element(Script.OP_PUSHDATA1.val, pub))
                .append(new Script.Element(Script.OP_DUP.val))
                .append(new Script.Element(Script.OP_HASH160.val))
                .append(new Script.Element(Script.OP_PUSHDATA1.val, Utils.sha256hash160(Sha256Hash.hash(pub))))
                .append(new Script.Element(Script.OP_EQUALVERIFY.val))
                .append(new Script.Element(Script.OP_CHECKSIG.val))
                .build()
                .run();
    }

    @Test
    public void tmp() {
    }
}
