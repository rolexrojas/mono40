package com.gbh.movil.domain.pos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.misc.Result;

/**
 * {@link PosBridge POS}'s result representation.
 *
 * @author hecvasro
 */
public final class PosResult<D> extends Result<PosCode, D> {
  /**
   * Constructs a new result.
   *
   * @param code
   *   Result's code.
   */
  public PosResult(@NonNull PosCode code, @Nullable D data) {
    super(code, data);
  }

  /**
   * TODO
   *
   * @param code
   *   TODO
   */
  public PosResult(@NonNull PosCode code) {
    super(code);
  }

  public boolean isSuccessful() {
    return getCode().equals(PosCode.OK);
  }
}
