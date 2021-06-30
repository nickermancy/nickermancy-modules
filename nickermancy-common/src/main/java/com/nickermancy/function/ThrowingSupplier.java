package com.nickermancy.function;

public interface ThrowingSupplier<T> {

    T get() throws Exception;
}
