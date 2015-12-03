package com.security;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.security.PrivilegedAction;

/**
 *
 *
 * Created by yjh on 15-12-2.
 */
public class AuthTEST {
    public static void main(String[] args) {
        System.setSecurityManager(new SecurityManager());
        try {
            //选择一个登录策略，创建一个登录上下文
            LoginContext context = new LoginContext("Login1");
            context.login();
            //得到一个通过验证的个体
            Subject subject = context.getSubject();
            System.out.println("Subject" + subject);
            PrivilegedAction<String> action = new SysPropAction("user.home");
            //如果这个subject具有对应的Principal就可以执行相应的操作
            String result = Subject.doAsPrivileged(subject, action, null);
            System.out.println(result);
            context.logout();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
