package com.security.jaas;

import javax.security.auth.callback.*;
import java.io.IOException;

/**
 *
 * Created by yjh on 15-12-2.
 */
public class SimpleCallbackHandler implements CallbackHandler {
    private String name;
    private char[] password;

    public SimpleCallbackHandler(String name, char[] password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for(Callback callback : callbacks) {
            if(callback instanceof NameCallback) {
                ((NameCallback) callback).setName(name);
            } else if(callback instanceof PasswordCallback) {
                ((PasswordCallback) callback).setPassword(password);
            }
        }
    }
}
