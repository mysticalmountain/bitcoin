package com.an.bitcoin.protocol;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.in;
import static java.lang.System.out;

/**
 * @ClassName TransactionInputTest
 * @Description TransactionInputTest
 * @Author an
 * @Date 2019/5/16 下午5:12
 * @Version 1.0
 */
public class TransactionInputTest {

    @Test
    public void serialize() throws IOException {
        Transaction transactiontion = new Transaction();
        TransactionInput input = new TransactionInput();
        TransactionOutPoint outPoint = new TransactionOutPoint(null, 0);
        byte [] scriptBytes = new byte[32];
        input.setScriptBytes(scriptBytes);
        input.setScriptLength(scriptBytes.length);
        input.setTransactionOutPoint(outPoint);

        out.println(input.getLength());
//        out.println(input.doSerialize().length);
        byte [] inputBytes = input.serialize();

        TransactionInput input1 = new TransactionInput(inputBytes, 0);

        Assert.assertEquals(input.getLength(), input1.getLength());

//        List<TransactionInput> inputs = new ArrayList<>();
//        inputs.add(input);
//        transaction.setInputs(inputs);
//        transaction.serialize();
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction)
    }
}
