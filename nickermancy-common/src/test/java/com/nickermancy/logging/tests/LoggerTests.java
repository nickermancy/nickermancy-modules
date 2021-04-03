package com.nickermancy.logging.tests;

import com.nickermancy.logging.Logger;

import java.lang.reflect.Proxy;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.util.ObjectUtils.isEmpty;

class LoggerTests {

    @TestFactory
    @DisplayName("Logger Factory Methods")
    Stream<DynamicTest> getLogger() {
        return Stream.of(
            dynamicTest("Logger.getLogger(String)", () ->
                assertThat(Logger.getLogger(MyClass.class.getName()).getName()).isEqualTo(MyClass.class.getName())),
            dynamicTest("Logger.getLogger(Class<?>)", () ->
                assertThat(Logger.getLogger(MyClass.class).getName()).isEqualTo(MyClass.class.getName())),
            dynamicTest("Logger.getLogger() (from static field)", () ->
                assertThat(MyClass.loggerFromField.getName()).isEqualTo(MyClass.class.getName())),
            dynamicTest("Logger.getLogger() (from lambda)", () ->
                assertThat(MyClass.loggerFromLambda().get().getName()).isEqualTo(MyClass.class.getName())),
            dynamicTest("Logger.getLogger() (from anonymous inner class)", () ->
                assertThat(MyClass.loggerFromAnonymousInnerClass().get().getName()).isEqualTo(MyClass.class.getName())),
            dynamicTest("Logger.getLogger() (from proxy handler)", () ->
                assertThat(MyClass.loggerFromProxy().get().getName()).isEqualTo(MyClass.class.getName())),
            dynamicTest("wrapping existing logger", () ->
                assertThat(MyClass.loggerFromLogger.getName()).isEqualTo(String.class.getName()))
        );
    }

    @TestFactory
    @DisplayName("API Methods")
    Stream<DynamicTest> loggerApi() {
        final var logger = Logger.getLogger();
        final var marker = MarkerFactory.getMarker("test marker");
        final var throwable = new Throwable();
        final var message = "test message";
        final var message1 = "test message {}";
        final var message2 = "test message {} {}";
        final var message3 = "test message {} {} {}";
        final var arg1 = 1;
        final var arg2 = 2;
        final var argArray = new Object[]{ 1, 2, 3 };
        final var messageSupplier = (Supplier<String>) () -> String.format("supplied message %d %d %d", argArray);
        return Stream.of(
            // LOGGER ERROR OPERATIONS
            dynamicTest("Logger.isErrorEnabled()", () ->
                assertThat(logger.isErrorEnabled()).isEqualTo(true)),
            dynamicTest("Logger.isErrorEnabled(Marker)", () ->
                assertThat(logger.isErrorEnabled(marker)).isEqualTo(true)),
            dynamicTest("Logger.error(String, Object)", () ->
                assertThatCode(() -> logger.error(message1, arg1)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(String, Object, Object)", () ->
                assertThatCode(() -> logger.error(message2, arg1, arg2)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(String, Object[])", () ->
                assertThatCode(() -> logger.error(message3, argArray)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(String)", () ->
                assertThatCode(() -> logger.error(message)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(String, Throwable)", () ->
                assertThatCode(() -> logger.error(message, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(Supplier<String>)", () ->
                assertThatCode(() -> logger.error(messageSupplier)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(Supplier<String>, Throwable)", () ->
                assertThatCode(() -> logger.error(messageSupplier, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(Marker, String, Object)", () ->
                assertThatCode(() -> logger.error(marker, message1, arg1)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(Marker, String, Object, Object)", () ->
                assertThatCode(() -> logger.error(marker, message2, arg1, arg2)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(Marker, String, Object[])", () ->
                assertThatCode(() -> logger.error(marker, message3, argArray)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(Marker, String)", () ->
                assertThatCode(() -> logger.error(marker, message)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(Marker, String, Throwable)", () ->
                assertThatCode(() -> logger.error(marker, message, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(Marker, Supplier<String>)", () ->
                assertThatCode(() -> logger.error(marker, messageSupplier)).doesNotThrowAnyException()),
            dynamicTest("Logger.error(Marker, Supplier<String>, Throwable)", () ->
                assertThatCode(() -> logger.error(marker, messageSupplier, throwable)).doesNotThrowAnyException()),

            // LOGGER WARN OPERATIONS
            dynamicTest("Logger.isWarnEnabled()", () ->
                assertThat(logger.isWarnEnabled()).isEqualTo(true)),
            dynamicTest("Logger.isWarnEnabled(Marker)", () ->
                assertThat(logger.isWarnEnabled(marker)).isEqualTo(true)),
            dynamicTest("Logger.warn(String, Object)", () ->
                assertThatCode(() -> logger.warn(message1, arg1)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(String, Object, Object)", () ->
                assertThatCode(() -> logger.warn(message2, arg1, arg2)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(String, Object[])", () ->
                assertThatCode(() -> logger.warn(message3, argArray)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(String)", () ->
                assertThatCode(() -> logger.warn(message)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(String, Throwable)", () ->
                assertThatCode(() -> logger.warn(message, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(Supplier<String>)", () ->
                assertThatCode(() -> logger.warn(messageSupplier)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(Supplier<String>, Throwable)", () ->
                assertThatCode(() -> logger.warn(messageSupplier, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(Marker, String, Object)", () ->
                assertThatCode(() -> logger.warn(marker, message1, arg1)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(Marker, String, Object, Object)", () ->
                assertThatCode(() -> logger.warn(marker, message2, arg1, arg2)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(Marker, String, Object[])", () ->
                assertThatCode(() -> logger.warn(marker, message3, argArray)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(Marker, String)", () ->
                assertThatCode(() -> logger.warn(marker, message)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(Marker, String, Throwable)", () ->
                assertThatCode(() -> logger.warn(marker, message, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(Marker, Supplier<String>)", () ->
                assertThatCode(() -> logger.warn(marker, messageSupplier)).doesNotThrowAnyException()),
            dynamicTest("Logger.warn(Marker, Supplier<String>, Throwable)", () ->
                assertThatCode(() -> logger.warn(marker, messageSupplier, throwable)).doesNotThrowAnyException()),

            // LOGGER INFO OPERATIONS
            dynamicTest("Logger.isInfoEnabled()", () ->
                assertThat(logger.isInfoEnabled()).isEqualTo(true)),
            dynamicTest("Logger.isInfoEnabled(Marker)", () ->
                assertThat(logger.isInfoEnabled(marker)).isEqualTo(true)),
            dynamicTest("Logger.info(String, Object)", () ->
                assertThatCode(() -> logger.info(message1, arg1)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(String, Object, Object)", () ->
                assertThatCode(() -> logger.info(message2, arg1, arg2)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(String, Object[])", () ->
                assertThatCode(() -> logger.info(message3, argArray)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(String)", () ->
                assertThatCode(() -> logger.info(message)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(String, Throwable)", () ->
                assertThatCode(() -> logger.info(message, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(Supplier<String>)", () ->
                assertThatCode(() -> logger.info(messageSupplier)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(Supplier<String>, Throwable)", () ->
                assertThatCode(() -> logger.info(messageSupplier, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(Marker, String, Object)", () ->
                assertThatCode(() -> logger.info(marker, message1, arg1)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(Marker, String, Object, Object)", () ->
                assertThatCode(() -> logger.info(marker, message2, arg1, arg2)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(Marker, String, Object[])", () ->
                assertThatCode(() -> logger.info(marker, message3, argArray)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(Marker, String)", () ->
                assertThatCode(() -> logger.info(marker, message)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(Marker, String, Throwable)", () ->
                assertThatCode(() -> logger.info(marker, message, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(Marker, Supplier<String>)", () ->
                assertThatCode(() -> logger.info(marker, messageSupplier)).doesNotThrowAnyException()),
            dynamicTest("Logger.info(Marker, Supplier<String>, Throwable)", () ->
                assertThatCode(() -> logger.info(marker, messageSupplier, throwable)).doesNotThrowAnyException()),

            // LOGGER DEBUG OPERATIONS
            dynamicTest("Logger.isDebugEnabled()", () ->
                assertThat(logger.isDebugEnabled()).isEqualTo(true)),
            dynamicTest("Logger.isDebugEnabled(Marker)", () ->
                assertThat(logger.isDebugEnabled(marker)).isEqualTo(true)),
            dynamicTest("Logger.debug(String, Object)", () ->
                assertThatCode(() -> logger.debug(message1, arg1)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(String, Object, Object)", () ->
                assertThatCode(() -> logger.debug(message2, arg1, arg2)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(String, Object[])", () ->
                assertThatCode(() -> logger.debug(message3, argArray)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(String)", () ->
                assertThatCode(() -> logger.debug(message)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(String, Throwable)", () ->
                assertThatCode(() -> logger.debug(message, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(Supplier<String>)", () ->
                assertThatCode(() -> logger.debug(messageSupplier)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(Supplier<String>, Throwable)", () ->
                assertThatCode(() -> logger.debug(messageSupplier, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(Marker, String, Object)", () ->
                assertThatCode(() -> logger.debug(marker, message1, arg1)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(Marker, String, Object, Object)", () ->
                assertThatCode(() -> logger.debug(marker, message2, arg1, arg2)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(Marker, String, Object[])", () ->
                assertThatCode(() -> logger.debug(marker, message3, argArray)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(Marker, String)", () ->
                assertThatCode(() -> logger.debug(marker, message)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(Marker, String, Throwable)", () ->
                assertThatCode(() -> logger.debug(marker, message, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(Marker, Supplier<String>)", () ->
                assertThatCode(() -> logger.debug(marker, messageSupplier)).doesNotThrowAnyException()),
            dynamicTest("Logger.debug(Marker, Supplier<String>, Throwable)", () ->
                assertThatCode(() -> logger.debug(marker, messageSupplier, throwable)).doesNotThrowAnyException()),

            // LOGGER TRACE OPERATIONS
            dynamicTest("Logger.isTraceEnabled()", () ->
                assertThat(logger.isTraceEnabled()).isEqualTo(true)),
            dynamicTest("Logger.isTraceEnabled(Marker)", () ->
                assertThat(logger.isTraceEnabled(marker)).isEqualTo(true)),
            dynamicTest("Logger.trace(String, Object)", () ->
                assertThatCode(() -> logger.trace(message1, arg1)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(String, Object, Object)", () ->
                assertThatCode(() -> logger.trace(message2, arg1, arg2)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(String, Object[])", () ->
                assertThatCode(() -> logger.trace(message3, argArray)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(String)", () ->
                assertThatCode(() -> logger.trace(message)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(String, Throwable)", () ->
                assertThatCode(() -> logger.trace(message, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(Supplier<String>)", () ->
                assertThatCode(() -> logger.trace(messageSupplier)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(Supplier<String>, Throwable)", () ->
                assertThatCode(() -> logger.trace(messageSupplier, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(Marker, String, Object)", () ->
                assertThatCode(() -> logger.trace(marker, message1, arg1)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(Marker, String, Object, Object)", () ->
                assertThatCode(() -> logger.trace(marker, message2, arg1, arg2)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(Marker, String, Object[])", () ->
                assertThatCode(() -> logger.trace(marker, message3, argArray)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(Marker, String)", () ->
                assertThatCode(() -> logger.trace(marker, message)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(Marker, String, Throwable)", () ->
                assertThatCode(() -> logger.trace(marker, message, throwable)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(Marker, Supplier<String>)", () ->
                assertThatCode(() -> logger.trace(marker, messageSupplier)).doesNotThrowAnyException()),
            dynamicTest("Logger.trace(Marker, Supplier<String>, Throwable)", () ->
                assertThatCode(() -> logger.trace(marker, messageSupplier, throwable)).doesNotThrowAnyException())
        );
    }

    private static class MyClass {

        static final Logger loggerFromField = Logger.getLogger();

        static final Logger loggerFromLogger = Logger.getLogger(LoggerFactory.getLogger(String.class));

        static Supplier<Logger> loggerFromLambda() {
            return Logger::getLogger;
        }

        static Supplier<Logger> loggerFromAnonymousInnerClass() {
            return new Provider<>() {

                @Override
                public Logger get() {
                    return Logger.getLogger();
                }
            };
        }

        @SuppressWarnings("unchecked")
        static Supplier<Logger> loggerFromProxy() {
            final var provider = new Provider<>() {

                @Override
                public Object get() {
                    return null;
                }
            };
            return (Supplier<Logger>) Proxy.newProxyInstance(MyClass.class.getClassLoader(), new Class[]{ Supplier.class }, (proxy, method, args) -> {
                if ("get".equals(method.getName()) && isEmpty(args)) {
                    return Logger.getLogger();
                }
                return method.invoke(provider, args);
            });
        }
    }

    private static abstract class Provider<T> implements Supplier<T> {

    }
}
