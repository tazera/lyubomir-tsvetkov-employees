package com.example.demo.service.internal;

import java.util.Objects;

  public final class PairKey {
    public final long first;  // smaller employee ID
    public final long second; // larger employee ID

    public PairKey(long a, long b) {
        if (a < b) {
            first = a;
            second = b;
        } else {
            first = b;
            second = a;
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PairKey k && first == k.first && second == k.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}