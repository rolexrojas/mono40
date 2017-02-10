package com.tpago.movil.d.misc.rx;

import android.support.annotation.NonNull;

import com.tpago.movil.d.misc.Mapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action2;
import rx.functions.Func0;
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
   * @return TODO
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

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static <T> Observable.Transformer<T, Set<T>> toSet() {
    return new Observable.Transformer<T, Set<T>>() {
      @Override
      public Observable<Set<T>> call(Observable<T> observable) {
        return observable.collect(new Func0<Set<T>>() {
          @Override
          public Set<T> call() {
            return new HashSet<>();
          }
        }, new Action2<Set<T>, T>() {
          @Override
          public void call(Set<T> set, T item) {
            set.add(item);
          }
        });
      }
    };
  }

  /**
   * TODO
   *
   * @param mapper
   *   TODO
   * @param <A>
   *   TODO
   * @param <B>
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static <A, B> Func1<A, B> mapperFunc(@NonNull final Mapper<A, B> mapper) {
    return new Func1<A, B>() {
      @Override
      public B call(A a) {
        return mapper.map(a);
      }
    };
  }
}
