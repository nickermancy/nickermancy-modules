package com.nickermancy.reactive;

import com.nickermancy.function.ThrowingSupplier;

import java.util.stream.Stream;

import reactor.core.publisher.Flux;

public class FluxSupport {

    public static <T> Flux<T> fromStream(ThrowingSupplier<Stream<T>> supplier) {
        return MonoSupport.fromSupplier(supplier).flatMapMany(Flux::fromStream);
    }
}
