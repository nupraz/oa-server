package com.graduation.oa.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class EncryptUtils {
    protected static final Log logger = LogFactory.getLog(com.bestvike.commons.utils.EncryptUtils.class);
    private static final String DES = "DES";

    public EncryptUtils() {
    }

    public static String MD5Encode(String sourceString) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return byte2hex(md.digest(sourceString.getBytes()));
    }

    public static byte[] MD5Encode(byte[] source) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(source);
    }

    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        byte[] var6 = array;
        int var7 = array.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            byte item = var6[var8];
            sb.append(Integer.toHexString(item & 255 | 256).substring(1, 3));
        }

        return sb.toString().toUpperCase();
    }

    public static String byte2hex(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);

        for(int i = 0; i < bytes.length; ++i) {
            if ((bytes[i] & 255) < 16) {
                buf.append("0");
            }

            buf.append(Long.toString((long)(bytes[i] & 255), 16));
        }

        return buf.toString();
    }

    public static byte[] hex2byte(String str) {
        if (str == null) {
            return null;
        } else {
            str = str.trim();
            int len = str.length();
            if (len != 0 && len % 2 != 1) {
                byte[] b = new byte[len / 2];

                try {
                    for(int i = 0; i < str.length(); i += 2) {
                        Integer ing = Integer.decode("0x" + str.substring(i, i + 2));
                        b[i / 2] = (byte)ing.intValue();
                    }

                    return b;
                } catch (Exception var4) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public static byte[] desEncrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(1, securekey, sr);
        return cipher.doFinal(src);
    }

    public static byte[] desDecrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(2, securekey, sr);
        return cipher.doFinal(src);
    }

    public static byte[] MD5HMAC(byte[] key, byte[] data) throws NoSuchAlgorithmException {
        int length = 64;
        byte[] ipad = new byte[length];
        byte[] opad = new byte[length];

        for(int i = 0; i < 64; ++i) {
            ipad[i] = 54;
            opad[i] = 92;
        }

        byte[] actualKey = key;
        byte[] keyArr = new byte[length];
        if (key.length > length) {
            actualKey = MD5Encode(key);
        }

        for(int i = 0; i < actualKey.length; ++i) {
            keyArr[i] = actualKey[i];
        }

        if (actualKey.length < length) {
            for(int i = actualKey.length; i < keyArr.length; ++i) {
                keyArr[i] = 0;
            }
        }

        byte[] kIpadXorResult = new byte[length];

        for(int j = 0; j < length; ++j) {
            kIpadXorResult[j] = (byte)(keyArr[j] ^ ipad[j]);
        }

        byte[] firstAppendResult = new byte[kIpadXorResult.length + data.length];

        for(int i = 0; i < kIpadXorResult.length; ++i) {
            firstAppendResult[i] = kIpadXorResult[i];
        }

        for(int i = 0; i < data.length; ++i) {
            firstAppendResult[i + keyArr.length] = data[i];
        }

        byte[] firstHashResult = MD5Encode(firstAppendResult);
        byte[] kOpadXorResult = new byte[length];

        for(int i = 0; i < length; ++i) {
            kOpadXorResult[i] = (byte)(keyArr[i] ^ opad[i]);
        }

        byte[] secondAppendResult = new byte[kOpadXorResult.length + firstHashResult.length];

        for(int i = 0; i < kOpadXorResult.length; ++i) {
            secondAppendResult[i] = kOpadXorResult[i];
        }

        for(int i = 0; i < firstHashResult.length; ++i) {
            secondAppendResult[i + keyArr.length] = firstHashResult[i];
        }

        byte[] hmacMd5Bytes = MD5Encode(secondAppendResult);
        return hmacMd5Bytes;
    }

    public static String desDecryptString(String originalString) {
        StringBuilder newString = new StringBuilder();

        try {
            String tempStr = new String(desDecrypt(hex2byte(originalString), "bv2013fr".getBytes()));
            newString.append(tempStr);
        } catch (Exception var3) {
            newString.append(originalString);
        }

        return newString.toString();
    }

    public static String AesEncrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        } else if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        } else {
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            return (new Base64()).encodeToString(encrypted);
        }
    }

    public static String AesDecrypt(String sSrc, String sKey) throws Exception {
        try {
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            } else if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            } else {
                byte[] raw = sKey.getBytes("utf-8");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(2, skeySpec);
                byte[] encrypted1 = (new Base64()).decode(sSrc);

                try {
                    byte[] original = cipher.doFinal(encrypted1);
                    String originalString = new String(original, "utf-8");
                    return originalString;
                } catch (Exception var8) {
                    System.out.println(var8.toString());
                    return null;
                }
            }
        } catch (Exception var9) {
            System.out.println(var9.toString());
            return null;
        }
    }

    public static String simpleEncrypt(String sSrc) {
        byte[] bas = sSrc.getBytes();

        int half;
        for(half = 0; half < bas.length; ++half) {
            bas[half] = (byte)(~bas[half]);
        }

        half = bas.length / 2;

        for(int i = 0; i < half; ++i) {
            if (i % 2 == 1) {
                byte b = bas[i];
                bas[i] = bas[i + half];
                bas[i + half] = b;
            }
        }

        return (new Base64()).encodeToString(bas);
    }

    public static String simpleDecrypt(String sSrc) {
        byte[] bas = (new Base64()).decode(sSrc);
        int half = bas.length / 2;

        int i;
        for(i = 0; i < half; ++i) {
            if (i % 2 == 1) {
                byte b = bas[i];
                bas[i] = bas[i + half];
                bas[i + half] = b;
            }
        }

        for(i = 0; i < bas.length; ++i) {
            bas[i] = (byte)(~bas[i]);
        }

        return new String(bas);
    }

    public static String base64Encode(String s) throws UnsupportedEncodingException {
        return (new Base64()).encodeToString(s.getBytes("utf-8"));
    }

    public static String base64Decode(String s) throws UnsupportedEncodingException {
        return new String((new Base64()).decode(s.getBytes("utf-8")));
    }
}