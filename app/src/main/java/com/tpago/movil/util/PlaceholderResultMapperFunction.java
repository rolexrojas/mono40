package com.tpago.movil.util;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author hecvasro
 */
public class PlaceholderResultMapperFunction<T> implements Function<Result<T>, Result<Placeholder>> {

  public static <T> PlaceholderResultMapperFunction<T> create() {
    return new PlaceholderResultMapperFunction<>();
  }

  private PlaceholderResultMapperFunction() {
  }

  @Override
  public Result<Placeholder> apply(@NonNull Result<T> result) throws Exception {
    if (result.isSuccessful()) {
      return Result.create(Placeholder.get());
    } else {
      return Result.create(result.failureData());
    }
  }
}
