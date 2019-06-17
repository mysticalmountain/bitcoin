package com.an.bitcoin.protocol;

import com.an.bitcoin.core.ECKey;
import com.an.bitcoin.core.Sha256Hash;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;

import static com.an.bitcoin.core.Utils.HEX;
import static java.lang.System.out;

/**
 * @ClassName ECKeyTest
 * @Description ECKeyTest
 * @Author an
 * @Date 2019/5/22 上午7:28
 * @Version 1.0
 */
public class ECKeyTest {

    @Test
    public void instantiate() {
        ECKey ecKey1 = new ECKey();
        BigInteger pri = ecKey1.getPrivKey();
        byte[] pubBytes = ecKey1.getPubKey();
        out.println(pri.toString(16));
        out.println(HEX.encode(pubBytes));
    }

    @Test
    public void instantiateFromPrivate() {
        ECKey ecKey1 = new ECKey();
        BigInteger pri = ecKey1.getPrivKey();
        byte[] pubBytes = ecKey1.getPubKey();

        ECKey ecKey2 = ECKey.fromPrivate(pri, false);
        BigInteger pri2 = ecKey2.getPrivKey();
        byte[] pubBytes2 = ecKey2.getPubKey();
        Assert.assertEquals(pri, pri2);
        Assert.assertEquals(HEX.encode(pubBytes), HEX.encode(pubBytes2));
    }

    @Test
    public void signature() throws IOException {
        ECKey ecKey = new ECKey();
        byte[] dataBytes = new byte[]{1};
        ECKey.ECDSASignature signature = ecKey.sign(Sha256Hash.wrap(Sha256Hash.hash(dataBytes)));
        out.println(HEX.encode(signature.encodeToDER()));
        out.println(HEX.encode(signature.derByteStream().toByteArray()));
        out.println(HEX.encode(ecKey.getPubKey()));
    }

    @Test
    public void verify() throws IOException {
        ECKey ecKey = new ECKey();
        byte[] dataBytes = new byte[]{1};
        ECKey.ECDSASignature signature = ecKey.sign(Sha256Hash.wrap(Sha256Hash.hash(dataBytes)));
        boolean res = ecKey.verify(Sha256Hash.hash(dataBytes), signature.derByteStream().toByteArray());
        Assert.assertTrue(res);

        byte[] errorDataBytes = new byte[]{2};
        boolean res2 = ecKey.verify(Sha256Hash.hash(errorDataBytes), signature.derByteStream().toByteArray());
        Assert.assertFalse(res2);
    }

    @Test
    public void verify1() throws IOException {
        ECKey ecKey = new ECKey();
        byte [] pub = ecKey.getPubKey();
        byte[] dataBytes = new byte[]{1};
        ECKey.ECDSASignature signature = ecKey.sign(Sha256Hash.wrap(Sha256Hash.hash(dataBytes)));

        ECKey target = ECKey.fromPublicOnly(pub);
        boolean res = target.verify(Sha256Hash.hash(dataBytes), signature.derByteStream().toByteArray());
        Assert.assertTrue(res);
    }

    @Test
    public void tmp() {
        String str = "AE9A0141";
        byte [] bytes = HEX.decode(str);
        out.println(new String(bytes));
    }
}
