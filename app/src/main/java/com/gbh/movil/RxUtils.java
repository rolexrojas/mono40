package com.gbh.movil;

import android.support.annotation.NonNull;

import java.util.Collection;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * ReactiveX utility methods.
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
   * @return TODo
   */
  @NonNull
  public static <T> Observable.Transformer<Collection<T>, T> fromCollection() {
    return new Observable.Transformer<Collection<T>, T>() {
      @Override
      public Observable<T> call(Observable<Collection<T>> observable) {
        return observable.flatMap(new Func1<Collection<T>, Observable<T>>() {
          @Override
          public Observable<T> call(Collection<T> list) {
            return Observable.from(list);
          }
        });
      }
    };
  }
}
