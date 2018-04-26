package com.tpago.movil.purchase;

import com.cube.sdk.Interfaces.CubeSdkCallback;
import com.cube.sdk.storage.operation.CubeError;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import io.reactivex.SingleEmitter;
import timber.log.Timber;

final class Cub3AddCardCallback implements CubeSdkCallback<String, CubeError> {

  static Cub3AddCardCallback create(SingleEmitter<Result<Placeholder>> emitter) {
    return new Cub3AddCardCallback(emitter);
  }

  private final SingleEmitter<Result<Placeholder>> emitter;

  private Cub3AddCardCallback(SingleEmitter<Result<Placeholder>> emitter) {
    this.emitter = ObjectHelper.checkNotNull(emitter, "emitter");
  }

  @Override
  public void success(String message) {
    Timber.i(message);

    this.emitter.onSuccess(Result.create(Placeholder.get()));
  }

  @Override
  public void failure(CubeError error) {
    this.emitter.onSuccess(Cub3PosServiceUtils.mapToResult(error));
  }
}
