package com.horbowicz.monads.java.io;

import java.util.function.Function;

public final class IO<T> {

  private final Operation<T> lazyOperation;

  private IO(Operation<T> lazyOperation) {
    this.lazyOperation = lazyOperation;
  }

  public <V> IO<V> map(Function<T, V> function) {
    return IO.of(() -> function.apply(run()));
  }

  public <V> IO<V> flatMap(Function<T, IO<V>> function) {
    return IO.of(() -> function.apply(run()).run());
  }

  public T run() {
    return lazyOperation.execute();
  }

  public interface Operation<V> {
    V execute();
  }

  public static <T> IO<T> of(Operation<T> lazyOperation) {
    return new IO<>(lazyOperation);
  }
}
