package com.an.bitcoin.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Block
 * @Description Block
 * @Author an
 * @Date 2019/5/9 下午4:26
 * @Version 1.0
 */
public class Block extends Message {

    private int version;
    private Sha256Hash prevBlockHash;
    private Sha256Hash merkleRootHash;
    private int time;
    private int difficultyTarget;
    private int nonce;
    private int txnCount;
    private List<Transaction> transactions;

    private Sha256Hash hash;

    public static final int HEADER_SIZE = 80;
    public static final long BLOCK_VERSION_GENESIS = 1;
    public static final long EASIEST_DIFFICULTY_TARGET = 0x207fFFFFL;


    public Block() {
    }

    public Block(byte[] payload) {
        super(payload);
    }

    @Override
    public void parse() throws ProtocolException {
        version = (int) readUint32();
        prevBlockHash = Sha256Hash.wrapReversed(readBytes(32));
        merkleRootHash = Sha256Hash.wrapReversed(readBytes(32));
        time = (int) readUint32();
        difficultyTarget = (int) readUint32();
        nonce = (int) readUint32();
        txnCount = (int) readVarInt();
        transactions = transactions == null ? new ArrayList<>() : transactions;
        for (int i = 0; i < txnCount; i++) {
            Transaction transaction = new Transaction(payload, cursor);
            transactions.add(transaction);
            cursor += transaction.getLength();
        }
    }

    @Override
    protected byte[] doSerialize() throws IOException, ProtocolException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        uint32ToByteStream(version, stream);
        stream.write(prevBlockHash.getReversedBytes());
        stream.write(merkleRootHash.getReversedBytes());
        uint32ToByteStream(time, stream);
        uint32ToByteStream(difficultyTarget, stream);
        uint32ToByteStream(nonce, stream);
        stream.write(new VarInt(txnCount).encode());
        for (Transaction transaction : transactions) {
            stream.write(transaction.doSerialize());
        }
        return stream.toByteArray();
    }

    @Override
    public String getCommand() {
        return "block";
    }

    @Override
    public int getLength() {
        int length = LENGTH_4           //version
                + 32                    //preBlockHash
                + 32                    //merkleRootHash
                + LENGTH_4              //time
                + LENGTH_4              //difficultyTarget
                + LENGTH_4              //nonce
                + new VarInt(txnCount).getOriginalSizeInBytes();        //txnCount
        for (Transaction transaction : transactions) {
            length += transaction.getLength();                          //transaction
        }
        return length;
    }

    @Override
    public int getChecksum() {
        return 0;
    }

    @Override
    public boolean support(Header header) {
        return getCommand().equals(header.command);
    }

    public void verify() {
        verifyHeader();
        verifyTransactions();
    }

    public void verifyHeader() throws VerificationException {
        try {
            checkProofOfWork(true);
        } catch (IOException e) {
            throw new ProtocolException("Unknown exception verify block header", e);
        }
    }

    public void verifyTransactions() {
        if (transactions.isEmpty())
            throw new VerificationException("Transaction is empty");
        //TODO verify block size

        Sha256Hash merkleRoot = calculateMerkleRoot();
        if (!merkleRoot.equals(merkleRootHash))
            throw new VerificationException(String.format("Merkle root hash not match %s vs %s", merkleRoot, merkleRootHash));
        //TODO verify sign

        //the first transaction of a block must be coinbase
        if (transactions.get(0).isCoinBase() == false) {
            throw new VerificationException("First transaction is not coinbase");
        }
        //from the second transaction not allow coinbase
        for (int i = 1; i < transactions.size(); i++) {
            if (transactions.get(i).isCoinBase())
                throw new VerificationException(String.format("Transaction %s not allow coinbase", transactions.get(i).getHash().toString()));
        }

        //TODO BIP-0034 Block v2 Height in coinbase

        for (Transaction transaction : transactions) {
            transaction.verify();
        }
    }

    public Sha256Hash getHash() {
        if (hash != null) {
            return hash;
        }
        try {
            hash = calculateHash();
            return hash;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mining() throws IOException {
        while (true) {
            if (checkProofOfWork(false)) return;
            setNonce(getNonce() + 1);
        }
    }


    /**
     * check proof of work is correct
     *
     * @param throwException is true, check fail throw VerificationException
     * @return
     * @throws VerificationException
     * @throws IOException
     */
    protected boolean checkProofOfWork(boolean throwException) throws VerificationException, IOException {
        BigInteger calcTarget = calculateHash().toBigInteger();
        BigInteger currentTarget = Utils.decodeCompactBits(difficultyTarget);
        boolean less = calcTarget.compareTo(currentTarget) < 0;
        if (less)
            return true;
        else if (!less && throwException)
            throw new VerificationException(String.format("Hash more than target %s vs %S", calcTarget.toString(16), currentTarget.toString(16)));
        else
            return false;
    }

    protected void checkTimestamp() throws VerificationException {
        long current = System.currentTimeMillis() / 1000;
        long difference = current - time;
        if (difference > 600) throw new VerificationException(String.format(""));
    }


    public BigInteger getDifficulty() throws IOException {
        BigInteger currentTarget = Utils.decodeCompactBits(difficultyTarget);
        BigInteger largestTarget = BigInteger.valueOf(1).shiftLeft(256);
        return largestTarget.divide(currentTarget);
    }

    public Sha256Hash calculateHash() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        uint32ToByteStream(version, stream);
        stream.write(prevBlockHash.getReversedBytes());
        stream.write(getMerkleRootHash().getReversedBytes());
        uint32ToByteStream(time, stream);
        uint32ToByteStream((int) difficultyTarget, stream);
        uint32ToByteStream(nonce, stream);
        return Sha256Hash.wrapReversed(Sha256Hash.hashTwice(stream.toByteArray()));
    }

    public Sha256Hash calculateMerkleRoot() {
        List<byte[]> merkleTree = new ArrayList<>();
        for (Transaction transaction : transactions) {
            merkleTree.add(transaction.getHash().getBytes());
        }
        buildMerkleTree(merkleTree);
        return Sha256Hash.wrap(merkleTree.get(merkleTree.size() - 1));
    }

    void buildMerkleTree(List<byte[]> datas) {
        int size = datas.size() + 1;                //每层元素个数
        int level = 0;                              //层数
        int sourceSize = datas.size();
        while (size >= 1) {
            level++;
            size /= 2;
            int dataSize = datas.size();
            int levelSize = sourceSize;             //当前层元素个数
            for (int i = 1; i < level; i++) {
                levelSize = (levelSize + 1) / 2;
            }
            if (levelSize == 1) {
                break;
            }
            for (int i = levelSize; i > 0; i -= 2) {
                byte[] left = datas.get(dataSize - i);
                byte[] right = new byte[32];
                if (i >= 2) {
                    right = datas.get(dataSize - i + 1);
                }
                if (levelSize == 1) {               //最上层
                    left = datas.get(dataSize - 2);
                    right = datas.get(dataSize - 1);
                }
                datas.add(Utils.reverseBytes(Sha256Hash.hashTwice(left, 0, 32, right, 0, 32)));
            }
        }
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Sha256Hash getPrevBlockHash() {
        return prevBlockHash;
    }

    public void setPrevBlockHash(Sha256Hash prevBlockHash) {
        this.prevBlockHash = prevBlockHash;
    }

    public Sha256Hash getMerkleRootHash() {
        merkleRootHash = calculateMerkleRoot();
        return merkleRootHash;
    }

    public void setMerkleRootHash(Sha256Hash merkleRootHash) {
        this.merkleRootHash = merkleRootHash;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getDifficultyTarget() {
        return difficultyTarget;
    }

    public void setDifficultyTarget(int difficultyTarget) {
        this.difficultyTarget = difficultyTarget;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public int getTxnCount() {
        return txnCount;
    }

    public void setTxnCount(int txnCount) {
        this.txnCount = txnCount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
