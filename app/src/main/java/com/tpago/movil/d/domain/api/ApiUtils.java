package com.tpago.movil.d.domain.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.misc.Utils;

import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public final class ApiUtils {
  private ApiUtils() {
  }

  /**
   * TODO
   *
   * @param assertData
   *   TODO
   * @param doOnFailure
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static <T> Observable.Transformer<ApiResult<T>, T> handleApiResult(
    final boolean assertData, @Nullable final Func1<ApiCode, Observable<T>> doOnFailure) {
    return new Observable.Transformer<ApiResult<T>, T>() {
      @Override
      public Observable<T> call(Observable<ApiResult<T>> observable) {
        return observable
          .flatMap(new Func1<ApiResult<T>, Observable<T>>() {
            @Override
            public Observable<T> call(ApiResult<T> result) {
              if (result.isSuccessful()) {
                final T data = result.getData();
                if (!assertData || Utils.isNotNull(data)) {
                  return Observable.just(data);
                } else {
                  return Observable.error(new NullPointerException("Result's data is missing"));
                }
              } else if (Utils.isNotNull(doOnFailure)) {
                return doOnFailure.call(result.getCode());
              } else {
                // TODO: Create a generic exception for these cases.
                return Observable.error(new Exception(result.toString()));
              }
            }
          });
      }
    };
  }

  /**
   * TODO
   *
   * @param assertData
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static <T> Observable.Transformer<ApiResult<T>, T> handleApiResult(
    final boolean assertData) {
    return handleApiResult(assertData, null);
  }
}
