package com.gbh.movil.data;

import android.support.annotation.NonNull;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class Delayer {
  /**
   * TODO
   */
  private static final Random RANDOM = new Random();

  private Delayer() {
  }

  /**
   * TODO
   *
   * @param <T>
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static <T> Observable.Transformer<T, T> apply() {
    return new Observable.Transformer<T, T>() {
      @Override
      public Observable<T> call(Observable<T> observable) {
        return observable.delay(RANDOM.nextInt(1000) + 500, TimeUnit.MILLISECONDS);
      }
    };
  }
}
