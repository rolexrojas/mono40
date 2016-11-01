package com.gbh.movil.data;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class RxUtils {
  private RxUtils() {
  }

  /**
   * TODO
   *
   * @param subscription
   *   TODO
   */
  public static void unsubscribe(@NonNull Subscription subscription) {
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
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
  public static <T> Func1<List<T>, Observable<T>> fromList() {
    return new Func1<List<T>, Observable<T>>() {
      @Override
      public Observable<T> call(List<T> list) {
        return Observable.from(list);
      }
    };
  }
}
