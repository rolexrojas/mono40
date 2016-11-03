package com.gbh.movil;

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

  public static void unsubscribe(@NonNull Subscription subscription) {
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  @NonNull
  public static <T> Observable.Transformer<List<T>, T> fromList() {
    return new Observable.Transformer<List<T>, T>() {
      @Override
      public Observable<T> call(Observable<List<T>> observable) {
        return observable.flatMap(new Func1<List<T>, Observable<T>>() {
          @Override
          public Observable<T> call(List<T> list) {
            return Observable.from(list);
          }
        });
      }
    };
  }
}
