package com.nickermancy.function;

public interface ThrowingConsumer<T> {

    void accept(T value) throws Exception;
}
