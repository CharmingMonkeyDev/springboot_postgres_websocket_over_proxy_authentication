package com.sunshineoxygen.inhome.utils;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * It contains security related methods like signContent.
 * @author ehizarci
 *
 */
public class Security {

    protected static char hex[] = { 	'0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd','e', 'f' };

    public static void main(String args[]) {
        try {
            //String str = digout("/a/bssdlfkjweljweirwr9238798ofsdjojsdlkjflsdjflsdkjlsdkfjlsdkjflsdkjflsdkfjsdlfjdlskfjdlskfjsdlfjlsdkfjldskfjdlskjsdlkfjdlkjsdlkj,cxmvsdnsldkfldskjfldskjfldkfjlsdkfjdlskjflsdkfjlsdkfjsdlkfjdlkfjdlkfjdslkfjsdlfjksdlfksdjflkdjflsdkfjsldkfjsdlkfjsdlkfjdslkfjsdflksdjflskdjfsldkfjsdlkjfsdlk23092309843094832094832049832049823403984302948204982094820948osjflsdflsdkjlsdkfjldskjsldkjflksdjfsdlkj");
            String rndStrin = RandomString.nextString(10);
            String str = digout(rndStrin);
            System.out.print(rndStrin+"="+str);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public static String digoutWithoutHex(String text) {
        if (isHexDigit(text))
            return text;
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        algorithm.reset();
        algorithm.update(text.getBytes());
        byte messageDigest[] = algorithm.digest();

        return new String(messageDigest);
    }

    public static String digout(String text){
        // If text has been converted already, return if self
        if (isHexDigit(text))
            return trimEmptySpace(text);

        //text = text.toUpperCase();
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        algorithm.reset();
        algorithm.update(text.getBytes());
        byte messageDigest[] = algorithm.digest();


        String hexCode = convertToHex(messageDigest).toUpperCase();

        hexCode = trimEmptySpace(hexCode);
        return hexCode;
    }

    public static String trimEmptySpace(String hexCode) {
        while (hexCode.indexOf("20")>0 && hexCode.indexOf("20")==hexCode.length()-2) {
            hexCode = hexCode.substring(0, hexCode.indexOf("20"));
        }
        return hexCode;
    }





    public static boolean isHexDigit(String hexDigit) {
        char[] hexDigitArray = hexDigit.toCharArray();
        int hexDigitLength = hexDigitArray.length;

        boolean isNotHex;
        for (int i = 0; i < hexDigitLength; i++) {
            isNotHex = Character.digit(hexDigitArray[i], 16) == -1;
            if (isNotHex) {
                return false;
            }
        }

        return true && hexDigit.length() >=20 &&  hexDigit.length() <= 32;
    }

    public static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String encodeHmacSHA256(String privateKey, String content) {
        String algorithm = "HmacSHA256";
        String publicKey = null;
        try {
            Mac hmac = Mac.getInstance(algorithm);
            SecretKeySpec spec = new SecretKeySpec(privateKey.getBytes(), algorithm);
            hmac.init(spec);
            publicKey = Hex.encodeHexString(hmac.doFinal(content.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return publicKey;
    }




}
