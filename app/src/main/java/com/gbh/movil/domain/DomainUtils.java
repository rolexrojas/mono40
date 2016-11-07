package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.functions.Func1;

/**
 * Domain utilities methods.
 *
 * @author hecvasro
 */
public final class DomainUtils {
  private DomainUtils() {
  }

  /**
   * TODO
   *
   * @param networkHelper
   *   TODO
   *
   * @return TODO
   */
  public static <D> Observable.Transformer<Result<DomainCode, D>, Result<DomainCode, D>>
  assertNetwork(@NonNull final NetworkHelper networkHelper) {
    return new Observable.Transformer<Result<DomainCode, D>, Result<DomainCode, D>>() {
      @Override
      public Observable<Result<DomainCode, D>> call(final Observable<Result<DomainCode, D>>
        observable) {
        return networkHelper.status()
          .first() // TODO: Explain why this operator is used.
          .flatMap(new Func1<Boolean, Observable<Result<DomainCode, D>>>() {
            @Override
            public Observable<Result<DomainCode, D>> call(Boolean available) {
              if (available) {
                return observable;
              } else {
                return Observable.just(Result.<DomainCode, D>create(DomainCode.FAILURE_NETWORK));
              }
            }
          });
      }
    };
  }

  /**
   * Indicates whether the given {@link Result} is successful or not.
   *
   * @param result
   *   {@link Result} that will be evaluated.
   *
   * @return True if it is successful, false otherwise.
   */
  public static <D> boolean isSuccessful(@NonNull Result<DomainCode, D> result) {
    return result.getCode() == DomainCode.SUCCESSFUL;
  }
}
