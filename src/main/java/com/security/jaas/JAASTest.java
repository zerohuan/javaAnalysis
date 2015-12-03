package com.security.jaas;

import com.security.SysPropAction;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Set;

/**
 * Created by yjh on 15-12-2.
 */
public class JAASTest {
    public static void main(String[] args) throws Exception {
        if(args.length < 2)
            throw new IllegalArgumentException("请传入用户名，密码");
        System.setSecurityManager(new SecurityManager());
        LoginContext context = new LoginContext("Login1", new SimpleCallbackHandler(args[0], args[1].toCharArray()));
        context.login();
        Subject subject = context.getSubject();
        PrivilegedAction<String> action = new SysPropAction("user.home");
        Set<Principal> principals = subject.getPrincipals();
        for(Principal p : principals) {
            System.out.println(p.getName());
        }
        String result = Subject.doAsPrivileged(subject, action, null);
        System.out.println(result);
        context.logout();
    }
}
