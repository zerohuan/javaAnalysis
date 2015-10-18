package com.yjh.initializationAndClean.singleton;

/**
 * Created by yjh on 15-10-18.
 */
public final class HasBuilder {
    private final int i1;
    private final int i2;
    private final String s1;

    public HasBuilder(Builder builder) {
        this.i1 = builder.i1;
        this.i2 = builder.i2;
        this.s1 = builder.s1;
    }

    public static class Builder {
        private int i1;
        private int i2;
        private String s1;

        public Builder i1(int i1) {
            this.i1 = i1;
            return this;
        }
        public Builder i2(int i2) {
            this.i2 = i2;
            return this;
        }
        public Builder s1(String s1) {
            this.s1 = s1;
            return this;
        }

        public HasBuilder build() {
            return new HasBuilder(this);
        }
    }
}
