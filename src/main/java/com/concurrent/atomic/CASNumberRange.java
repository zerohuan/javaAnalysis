package com.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by yjh on 16-1-11.
 */
public class CASNumberRange {
    private static final class IntPair {
        private final int lower;
        private final int upper;

        public IntPair(int lower, int upper) {
            if (lower >= upper)
                throw new IllegalArgumentException();
            this.lower = lower;
            this.upper = upper;
        }
    }

    private final AtomicReference<IntPair> intPair = new AtomicReference<>();

    public int getLower() {
        return intPair.get().lower;
    }

    public int getUpper() {
        return intPair.get().upper;
    }

    public void setLower(int i) {
        while(true) {
            IntPair oldPair = intPair.get();
            if (i > oldPair.upper)
                throw new IllegalArgumentException();
            IntPair newPair = new IntPair(i, oldPair.upper);
            if (intPair.compareAndSet(oldPair, newPair))
                return;
        }
    }

    public void setUpper(int i) {
        while (true) {
            IntPair oldPair = intPair.get();
            if (i < oldPair.lower)
                throw new IllegalArgumentException();
            IntPair newPair = new IntPair(oldPair.lower, i);
            if (intPair.compareAndSet(oldPair, newPair))
                return;
        }
    }
}
