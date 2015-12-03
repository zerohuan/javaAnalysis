package com.security.jaas;

import java.security.Principal;
import java.util.Objects;

/**
 *
 *
 * Created by yjh on 15-12-2.
 */
public class SimplePrincipal implements Principal {
    private String descr;
    private String value;

    public SimplePrincipal(String descr, String value) {
        this.descr = descr;
        this.value = value;
    }



    @Override
    public String getName() {
        return descr + "=" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof SimplePrincipal)) return false;
        SimplePrincipal that = (SimplePrincipal) o;

        return Objects.equals(that.getName(), getName());

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
