package com.net;

import java.io.IOException;

/**
 * 服务接口
 * Created by yjh on 15-12-1.
 */
public interface Server extends Runnable, AutoCloseable {
    void start() throws IOException;
}
