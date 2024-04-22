package com.webdev.cosmo.cosmobackend.util;

import com.webdev.cosmo.cosmobackend.util.interfaces.Validator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BetterOptional<T> {
    private final T base;

    public static <T> BetterOptional<T> of(T obj) {
        return new BetterOptional<>(obj);
    }

    public Optional<T> checkIfNotNull(Supplier<T> action, RuntimeException throwable) {
        if(action.get() == null) {
            throw throwable;
        }

        return Optional.of(action.get());
    }

    public BetterOptional<T> externalCheck(Supplier<?> action, RuntimeException throwable) {
        if(action.get() == null) {
            throw throwable;
        }

        return this;
    }

    public BetterOptional<T> peek(Consumer<T> action) {
        action.accept(base);

        return BetterOptional.of(base);
    }

    public Optional<T> filter(Predicate<T> condition) {
        return Optional.of(base)
                .filter(condition);
    }

    public BetterOptional<T> verify(Supplier<Boolean> condition, RuntimeException throwable) {
        if(!condition.get()) {
            throw throwable;
        }

        return this;
    }

    public Optional<T> checkIfNotEmpty(Supplier<Optional<T>> action, RuntimeException throwable) {
        if(action.get().isEmpty()) {
            throw throwable;
        }

        return action.get();
    }

    public BetterOptional<T> validate(Validator<T> validator, RuntimeException throwable) {
        if(!validator.exists(base)) {
            throw throwable;
        }

        return this;
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(base);
    }

    public <S> BetterOptional<S> map(S object) {
        return BetterOptional.of(object);
    }

    public <S> BetterOptional<S> map(Function<T, S> function) {
        return BetterOptional.of(function.apply(this.base));
    }

    public <S> Optional<S> optionalMap(Function<T, S> function) {
        return Optional.of(function.apply(this.base));
    }

}
