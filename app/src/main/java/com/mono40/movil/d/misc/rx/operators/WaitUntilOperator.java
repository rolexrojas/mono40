package com.mono40.movil.d.misc.rx.operators;

import androidx.annotation.NonNull;

import com.mono40.movil.util.ObjectHelper;

import rx.Observable;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public class WaitUntilOperator<T, O> implements Observable.Operator<T, T> {

  /**
   * TODO
   */
  private final Observable<O> observable;

  /**
   * TODO
   */
  private T lastT;

  /**
   * TODO
   *
   * @param observable
   *   TODO
   */
  public WaitUntilOperator(@NonNull Observable<O> observable) {
    this.observable = observable;
  }

  @Override
  public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
    final SerializedSubscriber<T> tSubscriber = new SerializedSubscriber<>(subscriber);
    final Subscriber<O> oSubscriber = new Subscriber<O>() {
      @Override
      public void onCompleted() {
        unsubscribe();
      }

      @Override
      public void onError(Throwable throwable) {
        tSubscriber.onError(throwable);
        tSubscriber.unsubscribe();
      }

      @Override
      public void onNext(O o) {
        if (ObjectHelper.isNotNull(lastT)) {
          tSubscriber.onNext(lastT);
        }
      }
    };
    subscriber.add(oSubscriber);
    observable.unsafeSubscribe(oSubscriber);
    return new Subscriber<T>(subscriber) {
      @Override
      public void onCompleted() {
        tSubscriber.onCompleted();
        unsubscribe();
      }

      @Override
      public void onError(Throwable throwable) {
        tSubscriber.onError(throwable);
        unsubscribe();
      }

      @Override
      public void onNext(T t) {
        lastT = t;
      }
    };
  }
}
