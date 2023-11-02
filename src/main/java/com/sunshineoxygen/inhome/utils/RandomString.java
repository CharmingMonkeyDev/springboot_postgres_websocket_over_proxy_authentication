package com.sunshineoxygen.inhome.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class RandomString {

    public static void main(String args[]) {
        System.out.println(RandomString.nextNumeric(10));
        int i = 0;
        while(i<5){
            i++;
            System.out.println("MSN"+RandomString.nextString(10));
        }

    }

    private static final char[] symbols;
    private static final char[] numericsymbols;

    static {
        StringBuilder tmp = new StringBuilder();
        StringBuilder tmpNumeric = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch){
            tmp.append(ch);
            tmpNumeric.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
        numericsymbols = tmpNumeric.toString().toCharArray();
    }

    public static String nextString(int length) {
        char[] buf = new char[length];
        Random random = new Random();
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf).toUpperCase();
    }

    public static String nextNumeric(int length) {
        char[] buf = new char[length];
        Random random = new Random();
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = numericsymbols[random.nextInt(numericsymbols.length)];
        return new String(buf).toUpperCase();
    }

    public static String generateRandomStringWithTimestampAsPrefix(int length) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        return sdf.format(new Date()).concat("-").concat(nextString(length));
    }

}