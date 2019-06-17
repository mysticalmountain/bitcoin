package com.an.bitcoin.protocol;

import com.an.bitcoin.core.Sha256Hash;
import com.an.bitcoin.core.VarInt;
import org.junit.Assert;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.an.bitcoin.protocol.Block.BLOCK_VERSION_GENESIS;
import static com.an.bitcoin.protocol.Block.EASIEST_DIFFICULTY_TARGET;
import static com.an.bitcoin.core.Utils.HEX;
import static java.lang.System.in;
import static java.lang.System.out;

/**
 * @ClassName BlockTest
 * @Description BlockTest
 * @Author an
 * @Date 2019/5/10 下午2:47
 * @Version 1.0
 */
public class BlockTest {


    public Block getGenesisBlock() {
        Block genesis = new Block();
        genesis.setVersion((int) BLOCK_VERSION_GENESIS);
        genesis.setPrevBlockHash(Sha256Hash.ZERO_HASH);
        genesis.setDifficultyTarget((int) EASIEST_DIFFICULTY_TARGET);
        genesis.setTime((int) (System.currentTimeMillis() / 1000));
        Transaction transaction = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        genesis.setTransactions(transactions);
        return genesis;
    }

    @Test
    public void getDifficulty() throws IOException {
        Block block = getGenesisBlock();
        BigInteger val = block.getDifficulty();
        out.println(val.floatValue());
    }

    @Test
    public void mining() throws IOException {
        Block genesisBlock = getGenesisBlock();
        Block block = new Block();
        block.setDifficultyTarget(0x2001FFFF);
        block.setPrevBlockHash(genesisBlock.getHash());
        block.setVersion(2);

        block.setTime((int) (System.currentTimeMillis() / 1000));

        Transaction transaction = new Transaction();
        transaction.setVersion(99);
        TransactionInput input = new TransactionInput();
        TransactionOutPoint outPoint = new TransactionOutPoint(null, 0);
        byte[] scriptBytes = new byte[10];
        input.setScriptBytes(scriptBytes);
        input.setScriptLength(new VarInt(scriptBytes.length).getOriginalSizeInBytes());
        input.setTransactionOutPoint(outPoint);
        List<TransactionInput> inputs = new ArrayList<>();
        inputs.add(input);


//        TransactionInput input2 = new TransactionInput();
//        TransactionOutPoint outPoint2 = new TransactionOutPoint(null, 0);
//        byte[] scriptBytes2 = new byte[10];
//        input2.setScriptBytes(scriptBytes2);
//        input2.setScriptLength(new VarInt(scriptBytes.length).getOriginalSizeInBytes());
//        input2.setTransactionOutPoint(outPoint2);
//        inputs.add(input2);


        transaction.setInputs(inputs);
        transaction.setTxInCount(inputs.size());

        TransactionOutput output = new TransactionOutput();
        byte [] outputScriptBytes = new byte[20];
        output.setScriptLength(new VarInt(outputScriptBytes.length).getOriginalSizeInBytes());
        output.setScriptBytes(outputScriptBytes);
        output.setValue(88);

        List<TransactionOutput> outputs = new ArrayList<>();
        outputs.add(output);
        transaction.setOutputs(outputs);
        transaction.setTxOutCount(outputs.size());

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        block.setTransactions(transactions);
        block.setTxnCount(transactions.size());

        block.mining();
        out.println(String.format("mining success nonce is %d", block.getNonce()));


        out.println(HEX.encode(block.serialize()));
    }

    @Test
    public void getHash() {
        Block genesisBlock = getGenesisBlock();
        Block block = new Block();
        block.setDifficultyTarget(0x2001FFFF);
        block.setPrevBlockHash(genesisBlock.getHash());
        block.setVersion(2);
        block.setTime((int) (System.currentTimeMillis() / 1000));
        Transaction transaction = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        block.setTransactions(transactions);
        Sha256Hash hash = block.getHash();
        out.println(hash.toBigInteger().toString(16));
        out.println(HEX.encode(hash.getBytes()));
    }

    @Test
    public void calculateTarget() {
        long difficulty = 0x1b0400cb;
        long power = difficulty >> 24;
        long low = difficulty & 0x00ffffff;
        long res = low * 2 << (8 * (power - 3));
        BigInteger bigInteger = BigInteger.valueOf(res);
        out.println(bigInteger.toString(16));
        out.println(bigInteger.toString(10));

        BigInteger b = BigInteger.valueOf((long) Math.pow(low, power));
        out.println(b.toString(16));
        out.println(b.toString(10));
    }


    @Test
    public void verifyHeader() {
        String hexBlock = "020000002793f9da5812d82fb539482e6491ed406d51580f96dde2c3cdb48be7bcec78ad43ec7a579f5561a42a7e9637ad4156672735a658be2752181801f723ba3316d20e1add5cffff01207f0000000001000000000000000000";
        Block block = new Block(HEX.decode(hexBlock));
        Transaction transaction = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        block.setTransactions(transactions);
        block.verifyHeader();
    }

    @Test
    public void verifyTransaction() {           //First transaction not is coinbase
        try {
            String hexBlock = "02000000959abfa2ade88fe613bd5ac325330de714164ee507805c2c5c90d32e82d926be1e3abf1cff63dbb8d3e798d0224913fdb87bfa3384fff3666aac65f2f7dad3fa7565dd5cffff0120370000000163000000010000000000000000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000001010000000000000001000000000000000000000000000000000000000000000000";
            Block block = new Block(HEX.decode(hexBlock));
//            Transaction transaction = new Transaction();
//            List<Transaction> transactions = new ArrayList<>();
//            transactions.add(transaction);
//            block.setTransactions(transactions);
            block.verifyTransactions();
        } catch (VerificationException e) {
            Assert.assertEquals(e.getMessage(), "First transaction is not coinbase");
        }
    }

    @Test
    public void verifyTransaction1() {           //From second transaction not allow coinbase
        try {
            String hexBlock = "020000009e24df4132a1ca788e84426145b76589bb3c5fde2000b7e07a5a7e1369096ac3b9eff6867218393695b7b4ed520e36ea2bad332d070d223eb2a8d6e0f20fb668d174dd5cffff0120120000000163000000020000000000000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000001580000000000000014000000000000000000000000000000000000000000000000";
            Block block = new Block(HEX.decode(hexBlock));
            block.verifyTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void serialize() {
        Block genesisBlock = getGenesisBlock();
        Block block = new Block();
        block.setDifficultyTarget(0x2001FFFF);
        block.setPrevBlockHash(genesisBlock.getHash());
        block.setVersion(2);
        block.setTime((int) (System.currentTimeMillis() / 1000));
        Transaction transaction = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        block.setTransactions(transactions);
        block.setTxnCount(transactions.size());
        block.getMerkleRootHash();
        byte[] blockBytes = block.serialize();
        out.println(HEX.encode(blockBytes));

        Block target = new Block(blockBytes);
        Assert.assertEquals(block.getVersion(), target.getVersion());
        Assert.assertEquals(block.getNonce(), target.getNonce());
        Assert.assertEquals(block.getTxnCount(), target.getTxnCount());
        target.getHash();
    }

    @Test
    public void serialize2() throws IOException {
        Block genesisBlock = getGenesisBlock();
        Block block = new Block();
        block.setDifficultyTarget(0x2001FFFF);
        block.setPrevBlockHash(genesisBlock.getHash());
        block.setVersion(2);
        block.setTime((int) (System.currentTimeMillis() / 1000));

        Transaction transaction = new Transaction();
        transaction.setVersion(99);

        TransactionInput input = new TransactionInput();
        TransactionOutPoint outPoint = new TransactionOutPoint(null, 0);
        byte[] scriptBytes = new byte[10];
        input.setScriptBytes(scriptBytes);
        input.setScriptLength(scriptBytes.length);
        input.setTransactionOutPoint(outPoint);
        List<TransactionInput> inputs = new ArrayList<>();
        inputs.add(input);
        transaction.setInputs(inputs);
        transaction.setTxInCount(inputs.size());

        TransactionOutput output = new TransactionOutput();
        byte [] outputScriptBytes = new byte[20];
        output.setScriptLength(outputScriptBytes.length);
        output.setScriptBytes(outputScriptBytes);
        output.setValue(88);
        List<TransactionOutput> outputs = new ArrayList<>();
        outputs.add(output);
        transaction.setOutputs(outputs);
        transaction.setTxOutCount(outputs.size());

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        block.setTransactions(transactions);
        block.setTxnCount(transactions.size());

        block.mining();


        byte [] blockBytes = block.serialize();
        out.println(HEX.encode(blockBytes));
        Block target = new Block(blockBytes);

        Assert.assertEquals(block.getVersion(), target.getVersion());
        Assert.assertEquals(block.getMerkleRootHash(), target.getMerkleRootHash());
    }

    @Test
    public void t2() {

        out.println(new VarInt(10).getOriginalSizeInBytes());
    }
}
