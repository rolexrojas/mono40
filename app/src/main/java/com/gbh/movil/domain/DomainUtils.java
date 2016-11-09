package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import rx.Observable;

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
  @Deprecated
  public static <D> Observable.Transformer<Result<DomainCode, D>, Result<DomainCode, D>>
  assertNetwork(@NonNull final NetworkHelper networkHelper) {
    return new Observable.Transformer<Result<DomainCode, D>, Result<DomainCode, D>>() {
      @Override
      public Observable<Result<DomainCode, D>> call(final Observable<Result<DomainCode, D>>
        observable) {
        return observable;
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
