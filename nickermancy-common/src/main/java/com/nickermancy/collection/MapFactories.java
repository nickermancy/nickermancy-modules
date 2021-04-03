package com.nickermancy.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("DuplicatedCode")
public class MapFactories {

    //-- Map Factories -----------------------------------------------------------------------------------------------//

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier) {
        return mapSupplier.get();
    }

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier, K k1, V v1) {
        final var map = mapSupplier.get();
        map.put(k1, v1);
        return map;
    }

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier, K k1, V v1, K k2, V v2) {
        final var map = mapSupplier.get();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier, K k1, V v1, K k2, V v2, K k3, V v3) {
        final var map = mapSupplier.get();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        final var map = mapSupplier.get();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        final var map = mapSupplier.get();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        final var map = mapSupplier.get();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        final var map = mapSupplier.get();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return map;
    }

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        final var map = mapSupplier.get();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        return map;
    }

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        final var map = mapSupplier.get();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        return map;
    }

    public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> mapSupplier, K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        final var map = mapSupplier.get();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        return map;
    }

    //-- HashMap Factories -------------------------------------------------------------------------------------------//

    public static <K, V> HashMap<K, V> mapOf() {
        return mapOf(HashMap::new);
    }

    public static <K, V> HashMap<K, V> mapOf(K k1, V v1) {
        return mapOf(HashMap::new, k1, v1);
    }

    public static <K, V> HashMap<K, V> mapOf(K k1, V v1, K k2, V v2) {
        return mapOf(HashMap::new, k1, v1, k2, v2);
    }

    public static <K, V> HashMap<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3) {
        return mapOf(HashMap::new, k1, v1, k2, v2, k3, v3);
    }

    public static <K, V> HashMap<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return mapOf(HashMap::new,
            k1, v1, k2, v2, k3, v3, k4, v4);
    }

    public static <K, V> HashMap<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return mapOf(HashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    public static <K, V> HashMap<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return mapOf(HashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    public static <K, V> HashMap<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return mapOf(HashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    public static <K, V> HashMap<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return mapOf(HashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    public static <K, V> HashMap<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return mapOf(HashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    public static <K, V> HashMap<K, V> mapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return mapOf(HashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }

    //-- LinkedHashMap Factories -------------------------------------------------------------------------------------//

    public static <K, V> LinkedHashMap<K, V> linkedMapOf() {
        return mapOf(LinkedHashMap::new);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMapOf(K k1, V v1) {
        return mapOf(LinkedHashMap::new, k1, v1);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMapOf(K k1, V v1, K k2, V v2) {
        return mapOf(LinkedHashMap::new, k1, v1, k2, v2);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMapOf(K k1, V v1, K k2, V v2, K k3, V v3) {
        return mapOf(LinkedHashMap::new, k1, v1, k2, v2, k3, v3);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return mapOf(LinkedHashMap::new,
            k1, v1, k2, v2, k3, v3, k4, v4);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return mapOf(LinkedHashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        return mapOf(LinkedHashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        return mapOf(LinkedHashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        return mapOf(LinkedHashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        return mapOf(LinkedHashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9);
    }

    public static <K, V> LinkedHashMap<K, V> linkedMapOf(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        return mapOf(LinkedHashMap::new, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10);
    }
}
