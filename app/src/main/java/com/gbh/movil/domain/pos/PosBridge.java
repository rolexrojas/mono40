package com.gbh.movil.domain.pos;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumber;

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
  Observable<PosResult<String>> addCard(@NonNull PhoneNumber phoneNumber, @NonNull String pin,
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
