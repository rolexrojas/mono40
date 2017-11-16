package com.tpago.movil.data.api;

import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Response;

/**
 * @author hecvasro
 */
final class RetrofitApiResultMapper<T> implements Function<Response<T>, Result<T>> {

  static Builder builder(RetrofitApiFailureDataMapper retrofitApiFailureDataMapper) {
    return new Builder(retrofitApiFailureDataMapper);
  }

  private final RetrofitApiFailureDataMapper retrofitApiFailureDataMapper;

  private RetrofitApiResultMapper(RetrofitApiFailureDataMapper retrofitApiFailureDataMapper) {
    this.retrofitApiFailureDataMapper = retrofitApiFailureDataMapper;
  }

  @Override
  public Result<T> apply(@NonNull Response<T> response) throws Exception {
    if (response.isSuccessful()) {
      return Result.create(response.body());
    } else {
      return Result.create(this.retrofitApiFailureDataMapper.apply(response.errorBody()));
    }
  }

  static final class Builder {

    private final RetrofitApiFailureDataMapper retrofitApiFailureDataMapper;

    private Builder(RetrofitApiFailureDataMapper retrofitApiFailureDataMapper) {
      this.retrofitApiFailureDataMapper = ObjectHelper
        .checkNotNull(retrofitApiFailureDataMapper, "retrofitApiFailureDataMapper");
    }

    final <T> RetrofitApiResultMapper<T> build() {
      return new RetrofitApiResultMapper<>(this.retrofitApiFailureDataMapper);
    }
  }
}
