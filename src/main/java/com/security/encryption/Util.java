package com.security.encryption;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

/**
 * Created by yjh on 15-12-3.
 */
public class Util {
    /**
     * 对输入流中读取的数据进行加密写入输出流
     *
     * @param in
     * @param out
     * @param cipher
     */
    public static void crypt(InputStream in, OutputStream out, Cipher cipher) throws IOException, GeneralSecurityException {
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(blockSize);
        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];

        boolean more = true;
        int inLen = 0;
        while(more) {
            inLen = in.read(inBytes);
            if(inLen == blockSize) {
                int outputLen = cipher.update(inBytes, 0, blockSize, outBytes);
                out.write(outBytes, 0, outputLen);
            } else {
                more = false;
            }
        }
        if(inLen > 0) outBytes = cipher.doFinal(inBytes, 0, inLen);
        else outBytes = cipher.doFinal();
        out.write(outBytes);
        out.flush();
    }
}
