package com.tpago.movil.purchase;

import com.cube.sdk.Interfaces.CubeSdkCallback;
import com.cube.sdk.storage.operation.CubeError;
import com.tpago.movil.util.ObjectHelper;

import io.reactivex.CompletableEmitter;
import timber.log.Timber;

final class Cub3DeleteCardCallback implements CubeSdkCallback<String, CubeError> {

  static Cub3DeleteCardCallback create(CompletableEmitter emitter) {
    return new Cub3DeleteCardCallback(emitter);
  }

  private final CompletableEmitter emitter;

  private Cub3DeleteCardCallback(CompletableEmitter emitter) {
    this.emitter = ObjectHelper.checkNotNull(emitter, "emitter");
  }

  @Override
  public void success(String message) {
    Timber.i(message);

    this.emitter.onComplete();
  }

  @Override
  public void failure(CubeError error) {
    this.emitter.onError(Cub3PosServiceException.create(error));
  }
}
