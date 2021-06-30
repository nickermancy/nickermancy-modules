package com.nickermancy.reactive;

import com.nickermancy.function.ThrowingConsumer;
import com.nickermancy.function.ThrowingRunnable;
import com.nickermancy.function.ThrowingSupplier;

import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

public class MonoSupport {

    public static Mono<Void> fromRunnable(ThrowingRunnable runnable) {
        return create(sink -> {
            runnable.run();
            sink.success();
        });
    }

    public static <T> Mono<T> fromSupplier(ThrowingSupplier<T> supplier) {
        return create(sink -> sink.success(supplier.get()));
    }

    public static <T> Mono<T> create(ThrowingConsumer<MonoSink<T>> callback) {
        return Mono.create(sink -> {
            try {
                callback.accept(sink);
            } catch (Exception e) {
                sink.error(e);
            }
        });
    }
}
