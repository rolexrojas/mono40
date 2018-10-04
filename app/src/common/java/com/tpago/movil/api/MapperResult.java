package com.tpago.movil.api;

import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * @author hecvasro
 */
final class MapperResult<T> implements Function<Response<T>, Result<T>> {

  static Creator creator(MapperFailureData mapper) {
    return new Creator(mapper);
  }

  private final MapperFailureData mapper;

  private MapperResult(MapperFailureData mapper) {
    this.mapper = mapper;
  }

  @Override
  public Result<T> apply(@NonNull Response<T> response) throws Exception {
    if (response.isSuccessful()) {
      return Result.create(response.body());
    }
    return Result.create(this.mapper.apply(response.errorBody()));
  }

  static final class Creator {

    private final MapperFailureData mapper;

    private Creator(MapperFailureData mapper) {
      this.mapper = ObjectHelper.checkNotNull(mapper, "mapper");
    }

    final <T> MapperResult<T> create() {
      return new MapperResult<>(this.mapper);
    }
  }
}
