package com.tpago.movil.domain.pos;

import android.support.annotation.NonNull;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface PosBridge {
  /**
   * TODO
   *
   * @param phoneNumber
   *   TODO
   * @param pin
   *   TODO
   * @param alias
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<PosResult<String>> addCard(@NonNull String phoneNumber, @NonNull String pin,
    @NonNull String alias);

  /**
   * TODO
   *
   * @param alias
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<PosResult<String>> selectCard(@NonNull String alias);

  /**
   * TODO
   *
   * @param alias
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<PosResult<String>> removeCard(@NonNull String alias);

  Observable<PosResult<String>> reset(@NonNull String phoneNumber);
}
