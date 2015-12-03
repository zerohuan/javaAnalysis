package com.security;

import java.security.PrivilegedAction;

/**
 * 只有通过主体特征的权限，才能构调用PrivilegedAction的run操作
 *
 * Created by yjh on 15-12-2.
 */
public class SysPropAction implements PrivilegedAction<String> {
    private String propertyName;

    public SysPropAction(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public String run() {
        return System.getProperty(propertyName);
    }
}
