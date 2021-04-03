package com.nickermancy.logging;

import java.util.function.Supplier;

import org.slf4j.Marker;

public interface Logger extends org.slf4j.Logger {

    static Logger getLogger() {
        return new LoggerImpl();
    }

    static Logger getLogger(String className) {
        return new LoggerImpl(className);
    }

    static Logger getLogger(Class<?> clazz) {
        return new LoggerImpl(clazz);
    }

    static Logger getLogger(org.slf4j.Logger logger) {
        return new LoggerImpl(logger);
    }

    default void error(Supplier<String> messageSupplier) {
        if (isErrorEnabled()) error(messageSupplier.get());
    }

    default void error(Supplier<String> messageSupplier, Throwable thrown) {
        if (isErrorEnabled()) error(messageSupplier.get(), thrown);
    }

    default void error(Marker marker, Supplier<String> messageSupplier) {
        if (isErrorEnabled()) error(marker, messageSupplier.get());
    }

    default void error(Marker marker, Supplier<String> messageSupplier, Throwable thrown) {
        if (isErrorEnabled()) error(marker, messageSupplier.get(), thrown);
    }

    default void warn(Supplier<String> messageSupplier) {
        if (isWarnEnabled()) warn(messageSupplier.get());
    }

    default void warn(Supplier<String> messageSupplier, Throwable thrown) {
        if (isWarnEnabled()) warn(messageSupplier.get(), thrown);
    }

    default void warn(Marker marker, Supplier<String> messageSupplier) {
        if (isWarnEnabled()) warn(marker, messageSupplier.get());
    }

    default void warn(Marker marker, Supplier<String> messageSupplier, Throwable thrown) {
        if (isWarnEnabled()) warn(marker, messageSupplier.get(), thrown);
    }

    default void info(Supplier<String> messageSupplier) {
        if (isInfoEnabled()) info(messageSupplier.get());
    }

    default void info(Supplier<String> messageSupplier, Throwable thrown) {
        if (isInfoEnabled()) info(messageSupplier.get(), thrown);
    }

    default void info(Marker marker, Supplier<String> messageSupplier) {
        if (isInfoEnabled()) info(marker, messageSupplier.get());
    }

    default void info(Marker marker, Supplier<String> messageSupplier, Throwable thrown) {
        if (isInfoEnabled()) info(marker, messageSupplier.get(), thrown);
    }

    default void debug(Supplier<String> messageSupplier) {
        if (isDebugEnabled()) debug(messageSupplier.get());
    }

    default void debug(Supplier<String> messageSupplier, Throwable thrown) {
        if (isDebugEnabled()) debug(messageSupplier.get(), thrown);
    }

    default void debug(Marker marker, Supplier<String> messageSupplier) {
        if (isDebugEnabled()) debug(marker, messageSupplier.get());
    }

    default void debug(Marker marker, Supplier<String> messageSupplier, Throwable thrown) {
        if (isDebugEnabled()) debug(marker, messageSupplier.get(), thrown);
    }

    default void trace(Supplier<String> messageSupplier) {
        if (isTraceEnabled()) trace(messageSupplier.get());
    }

    default void trace(Supplier<String> messageSupplier, Throwable thrown) {
        if (isTraceEnabled()) trace(messageSupplier.get(), thrown);
    }

    default void trace(Marker marker, Supplier<String> messageSupplier) {
        if (isTraceEnabled()) trace(marker, messageSupplier.get());
    }

    default void trace(Marker marker, Supplier<String> messageSupplier, Throwable thrown) {
        if (isTraceEnabled()) trace(marker, messageSupplier.get(), thrown);
    }
}
