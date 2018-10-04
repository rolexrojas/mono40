package com.tpago.movil.api;

import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Response;

final class EmptyMapperResult<T> implements Function<Response<T>, Result<T>> {

  static Creator creator(MapperFailureData mapper) {
    return new Creator(mapper);
  }

  private final MapperFailureData mapper;

  private EmptyMapperResult(MapperFailureData mapper) {
    this.mapper = mapper;
  }

  @Override
  public Result<T> apply(@NonNull Response<T> response) throws Exception {
    if (response.isSuccessful()) {
      return Result.createEmptySuccessResult((T) "{}");
    }
    return Result.create(this.mapper.apply(response.errorBody()));
  }

  static final class Creator {

    private final MapperFailureData mapper;

    private Creator(MapperFailureData mapper) {
      this.mapper = ObjectHelper.checkNotNull(mapper, "mapper");
    }

    final <T> EmptyMapperResult<T> create() {
      return new EmptyMapperResult<>(this.mapper);
    }
  }
}
