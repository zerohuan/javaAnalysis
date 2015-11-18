package com.basic.util.testUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 可测试对象的骨架类，不可实例化
 *
 * Created by yjh on 15-11-17.
 */
public abstract class AbstractTestable implements MTestable {
    private Logger logger = LogManager.getLogger();
    private final String name;

    public AbstractTestable(String name) {
        this.name = name;
    }

    @Override
    public abstract void test() throws Exception;

    public String getName() {
        return name;
    }
}
