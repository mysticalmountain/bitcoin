package com.an.bitcoin.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.out;

/**
 * @ClassName TreeTest
 * @Description TreeTest
 * @Author an
 * @Date 2019/5/13 上午8:39
 * @Version 1.0
 */
public class TreeTest {

    public static void main(String[] args) {

        List<String> list1 = new ArrayList<>();
        list1.add("A");
        list1.add("B");
        list1.add("C");
        list1.add("D");
        list1.add("E");
        list1.add("F");
        list1.add("G");
        list1.add("H");
        list1.add("I");
        list1.add("J");
        list1.add("K");
        list1.add("L");
        list1.add("M");
        list1.add("N");
        list1.add("O");
        list1.add("P");
        list1.add("Q");
        list1.add("R");
        list1.add("S");
        list1.add("T");

        TreeTest tt = new TreeTest();
        tt.buildMerkleTree(list1);
        list1.forEach(s -> out.print(s));
        out.println();

        out.println("-------" + list1.size());
    }

    void convertTree(List<Integer> source, List<Integer> target) {
        int flag = source.size() / 2;
        target.add(source.get(flag));

        List<Integer> s1 = new ArrayList<>();
        for (int i = 0; i < flag; i++) {
            s1.add(source.get(i));
        }
        if (s1.size() > 0) {
            convertTree(s1, target);
        }

        List<Integer> s2 = new ArrayList<>();
        for (int i = flag + 1; i < source.size(); i++) {
            s2.add(source.get(i));
        }
        if (s2.size() > 0) {
            convertTree(s2, target);
        }
    }

    void buildMerkleTree(List<String> list) {
        int size = list.size() + 1;

        if (list.size() % 2 == 1) {
            size--;
        }
        int times = 0;
        int sourceSize = list.size();
        int t = 0;
        while (size >= 1) {
            times++;
            size /= 2;
            if (times == 1) {
                for (int i = 0; i < list.size() - size; i += 2) {
                    out.println("========== i " + i);

                    String left = list.get(i);
                    String right = "#";
                    if (i + 1 < sourceSize) {
                        right = list.get(i + 1);
                    }
                    String str = left + right;
                    list.add(str);
                }
            } else {
                int s = list.size();
                t = sourceSize;
                for (int i = 1; i < times; i++) {
                    t = (t + 1) / 2;
                }
                out.println("################### " + t + "\t" + sourceSize + "\t" + (times));

                if (t == 1) {
                    break;
                }
                for (int i = t; i > 0; i -= 2) {

                    String left = list.get(s - i);
                    String right = "#";
                    if (i >= 2) {
                        right = list.get(s - i + 1);
                    }
                    if (t == 1) {
                        left = list.get(s - 2);
                        right = list.get(s - 1);
                    }
                    String str = left + right;

                    list.add(str);
                }
            }
        }
    }
}
