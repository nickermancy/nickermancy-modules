package com.nickermancy.core;

import java.util.Optional;
import java.util.function.Supplier;

public class Singleton<T> {

    private final Object lock = new Object();

    private boolean initialized;
    private Supplier<Optional<T>> singleton;

    public Singleton(Supplier<T> supplier) {
        this.singleton = () -> initializeFrom(supplier);
    }

    public T get() {
        return singleton.get().orElseThrow();
    }

    public T orElse(T defaultValue) {
        return singleton.get().orElse(defaultValue);
    }

    private Optional<T> initializeFrom(Supplier<T> supplier) {
        synchronized (lock) {
            if (!initialized) {
                T value = supplier.get();
                singleton = () -> Optional.ofNullable(value);
                initialized = true;
            }
        }
        return singleton.get();
    }
}
