package com.yjh.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by yjh on 15-12-8.
 */
public class FileUtil {
    public static File getFile(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }
}
