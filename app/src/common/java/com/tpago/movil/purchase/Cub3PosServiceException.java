package com.tpago.movil.purchase;

import com.cube.sdk.storage.operation.CubeError;
import com.tpago.movil.util.ObjectHelper;

final class Cub3PosServiceException extends Exception {

  static Cub3PosServiceException create(CubeError error) {
    return new Cub3PosServiceException(
      Cub3PosServiceUtils.errorMessage(
        ObjectHelper.checkNotNull(
          error,
          "error"
        )
      )
    );
  }

  private Cub3PosServiceException(String message) {
    super(message);
  }
}
