package com.guilhermezuriel.tendadesafiotecnico._shared.result;

public sealed interface Result<T, E> {

    record Success<T, E>(T value) implements Result<T, E> {}
    record Failure<T, E>(E error) implements Result<T, E> {}

    static <T, E> Result<T, E> ok(T value) {
        return new Success<>(value);
    }

    static <T, E> Result<T, E> fail(E error) {
        return new Failure<>(error);
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isFailure() {
        return this instanceof Failure;
    }

    default T getValue() {
        if (this instanceof Success<T, E> s) return s.value();
        throw new IllegalStateException("Cannot get value from Failure");
    }

    default E getError() {
        if (this instanceof Failure<T, E> f) return f.error();
        throw new IllegalStateException("Cannot get error from Success");
    }
}