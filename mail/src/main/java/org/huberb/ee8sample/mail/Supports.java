/*
 * Copyright 2023 berni3.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.huberb.ee8sample.mail;

import java.util.Objects;
import javax.mail.MessagingException;

/**
 * Classes supporting the functional style API.
 *
 * @author berni3
 */
public class Supports {

    private Supports() {
    }

    /**
     * Define a consumer for mail operations.
     * <p>
     * This consumer rethrows the {@link MessagingException} and don't try to
     * handle it.
     *
     * @param <T>
     * @author berni3
     */
    @FunctionalInterface
    public interface ConsumerThrowingMessagingException<T> {

        public static <T> ConsumerThrowingMessagingException<T> NOOP() {
            return t -> {
            };
        }

        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         * @throws MessagingException thrown if consumer fails
         */
        void accept(T t) throws MessagingException;

        /**
         * Returns a composed {@code Consumer} that performs, in sequence, this
         * operation followed by the {@code after} operation. If performing
         * either operation throws an exception, it is relayed to the caller of
         * the composed operation. If performing this operation throws an
         * exception, the {@code after} operation will not be performed.
         *
         * @param after the operation to perform after this operation
         * @return a composed {@code Consumer} that performs in sequence this
         * operation followed by the {@code after} operation
         * @throws NullPointerException if {@code after} is null
         */
        default ConsumerThrowingMessagingException<T> andThen(ConsumerThrowingMessagingException<? super T> after) {
            Objects.requireNonNull(after);
            return (T t) -> {
                accept(t);
                after.accept(t);
            };
        }

    }

    /**
     * Define a functions for mail operations.
     * <p>
     * This function rethrows the {@link MessagingException} and don't try to
     * handle it.
     *
     * @param <T> input type of the function
     * @param <R> output type of the function
     */
    @FunctionalInterface
    public interface FunctionThrowingMessagingException<T, R> {

        /**
         * Performs this operation on the given argument.
         *
         * @param t the input argument
         * @throws MessagingException thrown if operation fails
         */
        R apply(T t) throws MessagingException;

        /**
         * Returns a composed function that first applies the {@code before}
         * function to its input, and then applies this function to the result.
         * If evaluation of either function throws an exception, it is relayed
         * to the caller of the composed function.
         *
         * @param <V> the type of input to the {@code before} function, and to
         * the composed function
         * @param before the function to apply before this function is applied
         * @return a composed function that first applies the {@code before}
         * function and then applies this function
         * @throws NullPointerException if before is null
         * @see #andThen(FunctionThrowingMessagingException)
         */
        default <V> FunctionThrowingMessagingException<V, R> compose(FunctionThrowingMessagingException<? super V, ? extends T> before) {
            Objects.requireNonNull(before);
            return (V v) -> apply(before.apply(v));
        }

        /**
         * Returns a composed function that first applies this function to its
         * input, and then applies the {@code after} function to the result. If
         * evaluation of either function throws an exception, it is relayed to
         * the caller of the composed function.
         *
         * @param <V> the type of output of the {@code after} function, and of
         * the composed function
         * @param after the function to apply after this function is applied
         * @return a composed function that first applies this function and then
         * applies the {@code after} function
         * @throws NullPointerException if after is null
         * @see #compose(FunctionThrowingMessagingException)
         */
        default <V> FunctionThrowingMessagingException<T, V> andThen(FunctionThrowingMessagingException<? super R, ? extends V> after) {
            Objects.requireNonNull(after);
            return (T t) -> after.apply(apply(t));
        }

        /**
         * Returns a function that always returns its input argument.
         *
         * @param <T> the type of the input and output objects to the function
         * @return a function that always returns its input argument
         */
        static <T> FunctionThrowingMessagingException<T, T> identity() {
            return t -> t;
        }
    }

    /**
     * Define a run-time exception for wrapping any {@link  RuntimeException}.
     * <p>
     * Instead of throwing {@link  RuntimeException} in the API. The API wraps a
     * {@link RuntimeException} as {@link MailRuntimeException} in order to make
     * it more easier for a API use to catch it specifically.
     */
    public static class MailRuntimeException extends RuntimeException {

        public MailRuntimeException(String m) {
            super(m);
        }

        public MailRuntimeException(String m, Throwable cause) {
            super(m, cause);
        }
    }
}
