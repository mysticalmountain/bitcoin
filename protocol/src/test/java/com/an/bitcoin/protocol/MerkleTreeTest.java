package com.an.bitcoin.protocol;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * @ClassName MerkleTreeTest
 * @Description MerkleTreeTest
 * @Author an
 * @Date 2019/5/14 上午10:43
 * @Version 1.0
 */
public class MerkleTreeTest {

    public static void main(String[] args) {

        List<String> datas = new ArrayList<>();
        datas.add("A");
        datas.add("B");
        datas.add("C");
        datas.add("D");
        datas.add("E");
        datas.add("F");
        datas.add("G");
        datas.add("H");
        datas.add("I");
        datas.add("J");
        datas.add("K");
        datas.add("L");
        datas.add("M");
        datas.add("N");
        datas.add("O");
        datas.add("P");
        datas.add("Q");
        datas.add("R");
        datas.add("S");
        datas.add("T");
        datas.add("U");
        datas.add("V");
        datas.add("W");
        datas.add("X");
        datas.add("Y");
        datas.add("Z");


        MerkleTreeTest t = new MerkleTreeTest();
        t.buildMerkleTree(datas);
        datas.forEach(v -> out.println(v));
    }

    void buildMerkleTree(List<String> datas) {
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
            out.println("################### " + levelSize + "\t" + sourceSize + "\t" + (level));
            if (levelSize == 1) {
                break;
            }
            for (int i = levelSize; i > 0; i -= 2) {
                String left = datas.get(dataSize - i);
                String right = "#";                 //元素个数不够则用"#"填充
                if (i >= 2) {
                    right = datas.get(dataSize - i + 1);
                }
                if (levelSize == 1) {               //最上层
                    left = datas.get(dataSize - 2);
                    right = datas.get(dataSize - 1);
                }
                datas.add(left + right);
            }
        }
    }
}
