package com.tpago.movil.dep.misc;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
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
