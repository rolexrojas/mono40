package com.gbh.movil.domain.pos;

import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.List;

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
   * @return TODO
   */
  boolean isDefault();

  /**
   * TODO
   *
   * @return TODO
   */
  Intent requestToMakeDefault();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<PosResult<List<String>>> getCards();

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
  Observable<PosResult<Void>> selectCard(@NonNull String alias);

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
}
