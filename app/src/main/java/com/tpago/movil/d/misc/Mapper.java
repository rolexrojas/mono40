package com.tpago.movil.d.misc;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public interface Mapper<A, B> {
  /**
   * TODO
   *
   * @param a
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  B map(@NonNull A a);
}
