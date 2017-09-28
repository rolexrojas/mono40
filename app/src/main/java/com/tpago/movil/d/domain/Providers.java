package com.tpago.movil.d.domain;

import com.tpago.movil.api.ApiCode;

import java.util.Set;

import io.reactivex.functions.Function;

/**
 * @author hecvasro
 */
@Deprecated
public final class Providers {
  public static <T> Function<Result<Set<T>, ApiCode>, Result<Set<T>, ErrorCode>> resultMapper() {
    return new Function<Result<Set<T>, ApiCode>, Result<Set<T>, ErrorCode>>() {
      @Override
      public Result<Set<T>, ErrorCode> apply(Result<Set<T>, ApiCode> result) {
        if (result.isSuccessful()) {
          return Result.create(result.getSuccessData());
        } else {
          final FailureData<ApiCode> failureData = result.getFailureData();
          return Result.create(FailureData.create(ErrorCode.UNEXPECTED, failureData.getDescription()));
        }
      }
    };
  }

  private Providers() {
    throw new AssertionError("Cannot be constructed");
  }
}
