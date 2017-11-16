package com.tpago.movil.d.misc.rx.operators;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public class DoOnEmpty<T> implements Observable.Operator<T, T> {
  private final Action0 action;

  private boolean isEmpty = true;

  public DoOnEmpty(@NonNull Action0 action) {
    this.action = action;
  }

  @Override
  public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
    return new Subscriber<T>() {
      @Override
      public void onCompleted() {
        if (isEmpty) {
          action.call();
        }
        subscriber.onCompleted();
      }

      @Override
      public void onError(Throwable throwable) {
        subscriber.onError(throwable);
      }

      @Override
      public void onNext(T t) {
        isEmpty = false;
        subscriber.onNext(t);
      }
    };
  }
}
