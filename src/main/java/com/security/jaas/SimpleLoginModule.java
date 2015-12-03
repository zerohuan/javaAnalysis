package com.security.jaas;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 *
 * Created by yjh on 15-12-2.
 */
public class SimpleLoginModule implements LoginModule {
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, ?> options;


    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.options = options;
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback nameCallback = new NameCallback("username");
        PasswordCallback passwordCallback = new PasswordCallback("password", false);
        try {
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
            System.out.println("callbak set");
        } catch (IOException | UnsupportedCallbackException e) {
            throw new LoginException("login fail");
        }

        try {
            boolean flag = checkLogin(nameCallback, passwordCallback);
            return flag;
        } catch (Exception e) {
            System.out.println(e);
            throw new LoginException("login fail in checking");
        }
    }

    private boolean checkLogin(NameCallback nameCallback, PasswordCallback passwordCallback) throws IOException {
        try(Scanner scanner = new Scanner(Paths.get("" + options.get("pwdf")))) {
            String[] line;
            while(scanner.hasNextLine()) {
                line = scanner.nextLine().split("\\|");
                if(line.length < 3)
                    continue;
                String name = line[0];
                char[] password = line[1].toCharArray();
                String role = line[2];
                System.out.println(name + role + line[1] + " " + nameCallback.getName() + Arrays.toString(passwordCallback.getPassword()));
                if(name.equals(nameCallback.getName())
                        && Arrays.equals(password, passwordCallback.getPassword())) {
                    System.out.println("check success");
                    Set<Principal> principalSet = subject.getPrincipals();
                    principalSet.add(new SimplePrincipal("role", role));
                    principalSet.add(new SimplePrincipal("name", name));
                    System.out.println("check success*****************");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean commit() throws LoginException {
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return true;
    }
}
