package com.an.bitcoin.protocol;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.an.bitcoin.protocol.Utils.HEX;
import static java.lang.System.out;

/**
 * @ClassName TransactionTest
 * @Description TransactionTest
 * @Author an
 * @Date 2019/5/10 上午7:42
 * @Version 1.0
 */
public class TransactionTest {

    @Test
    public void printHex() {
        Transaction transaction = new Transaction();
        out.println(HEX.encode(transaction.serialize()));
    }

    @Test
    public void serialize() {
//        Transaction transaction = new Transaction();
//        byte [] transactionBytes = transaction.serialize();
//        Transaction targetTransaction = new Transaction(transactionBytes);
//        Assert.assertEquals(transaction.getVersion(), targetTransaction.getVersion());

        Transaction transaction = new Transaction();

        TransactionInput input = new TransactionInput();
        TransactionOutPoint outPoint = new TransactionOutPoint(null, 0);
        byte[] scriptBytes = new byte[10];
        input.setScriptBytes(scriptBytes);
        input.setScriptLength(new VarInt(scriptBytes.length).getOriginalSizeInBytes());
        input.setTransactionOutPoint(outPoint);
        List<TransactionInput> inputs = new ArrayList<>();
        inputs.add(input);


        TransactionInput input2 = new TransactionInput();
        TransactionOutPoint outPoint2 = new TransactionOutPoint(null, 0);
        byte[] scriptBytes2 = new byte[10];
        input2.setScriptBytes(scriptBytes2);
        input2.setScriptLength(new VarInt(scriptBytes.length).getOriginalSizeInBytes());
        input2.setTransactionOutPoint(outPoint2);
        inputs.add(input2);


        transaction.setInputs(inputs);
        transaction.setTxInCount(inputs.size());

        byte [] transactionBytes = transaction.serialize();

        Transaction transaction1 = new Transaction(transactionBytes);

        Assert.assertEquals(transaction.getVersion(), transaction1.getVersion());
    }

    @Test(expected = VerificationException.EmptyInputsOrOutputs.class)
    public void verifyEmptyInputsOrOutputs() {
        Transaction transaction = new Transaction();
        transaction.verify();
    }

    @Test(expected = VerificationException.DuplicatedOutPoint.class)
    public void verifyDuplicatedOutPoint() {
        TransactionOutPoint outPoint = new TransactionOutPoint();
        TransactionInput input1 = new TransactionInput();
        TransactionInput input2 = new TransactionInput();
        input1.setTransactionOutPoint(outPoint);
        input2.setTransactionOutPoint(outPoint);
        List<TransactionInput> inputs = new ArrayList<>();
        inputs.add(input1);
        inputs.add(input2);
        TransactionOutput output1 = new TransactionOutput();
        List<TransactionOutput> outputs = new ArrayList<>();
        outputs.add(output1);

        Transaction transaction = new Transaction();
        transaction.setInputs(inputs);
        transaction.setOutputs(outputs);
        transaction.verify();
    }

    @Test
    public void verify() {
        TransactionOutPoint outPoint = new TransactionOutPoint();
        TransactionInput input1 = new TransactionInput();
        input1.setTransactionOutPoint(outPoint);
        List<TransactionInput> inputs = new ArrayList<>();
        inputs.add(input1);
        TransactionOutput output1 = new TransactionOutput();
        List<TransactionOutput> outputs = new ArrayList<>();
        outputs.add(output1);

        Transaction transaction = new Transaction();
        transaction.setInputs(inputs);
        transaction.setOutputs(outputs);
        transaction.verify();
    }

    @Test
    public void hash() {
        Transaction transaction = new Transaction();
        transaction.getHash();
    }
}
