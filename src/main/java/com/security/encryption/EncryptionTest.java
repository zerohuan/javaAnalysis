package com.security.encryption;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Created by yjh on 15-12-3.
 */
public class EncryptionTest {
    public static void test() throws Exception {
        byte[] seq = "sdlkfjiuqoenrgiuerds,msfd".getBytes();
        SecretKey key = new SecretKeySpec(seq, "AES");
    }

    public static void main(String[] args) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //"真正的随机数"，加密的随机数，需要额外性能
        //Random以日期和时间为种子并不安全
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(secureRandom);
        Key key = keyGenerator.generateKey();

        Cipher aes = Cipher.getInstance("AES");
        int mode = Cipher.ENCRYPT_MODE;
        aes.init(mode, key);
        int blockSize = aes.getBlockSize();
        byte[] inputBytes = new byte[blockSize];
        int outputSize = aes.getOutputSize(blockSize);


    }
}
