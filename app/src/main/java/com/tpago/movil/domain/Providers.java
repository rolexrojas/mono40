package com.tpago.movil.domain;

import com.tpago.movil.api.ApiCode;

import java.util.Set;

import io.reactivex.functions.Function;

/**
 * @author hecvasro
 */
public final class Providers {
  public static <T> Function<Result<Set<T>, ApiCode>, Result<Set<T>, ProviderCode>> resultMapper() {
    return new Function<Result<Set<T>, ApiCode>, Result<Set<T>, ProviderCode>>() {
      @Override
      public Result<Set<T>, ProviderCode> apply(Result<Set<T>, ApiCode> result) {
        if (result.isSuccessful()) {
          return Result.create(result.getSuccessData());
        } else {
          final FailureData<ApiCode> failureData = result.getFailureData();
          return Result.create(FailureData.create(ProviderCode.UNEXPECTED, failureData.getDescription()));
        }
      }
    };
  }

  private Providers() {
    throw new AssertionError("Cannot be constructed");
  }
}
