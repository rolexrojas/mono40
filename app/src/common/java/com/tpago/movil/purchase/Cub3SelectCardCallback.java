package com.tpago.movil.purchase;

import com.cube.sdk.Interfaces.CubeSdkCallback;
import com.cube.sdk.storage.operation.CubeError;
import com.cube.sdk.storage.operation.PaymentInfo;
import com.tpago.movil.util.ObjectHelper;

import io.reactivex.CompletableEmitter;
import timber.log.Timber;

final class Cub3SelectCardCallback implements CubeSdkCallback<PaymentInfo, CubeError> {

  static Cub3SelectCardCallback create(CompletableEmitter emitter) {
    return new Cub3SelectCardCallback(emitter);
  }

  private final CompletableEmitter emitter;

  private Cub3SelectCardCallback(CompletableEmitter emitter) {
    this.emitter = ObjectHelper.checkNotNull(emitter, "emitter");
  }

  @Override
  public void success(PaymentInfo data) {
    Timber.i(Cub3PosServiceUtils.mapToString(data));

    this.emitter.onComplete();
  }

  @Override
  public void failure(CubeError error) {
    this.emitter.onError(Cub3PosServiceException.create(error));
  }
}
