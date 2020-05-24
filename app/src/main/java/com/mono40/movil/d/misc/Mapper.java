package com.mono40.movil.d.misc;

import androidx.annotation.NonNull;

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
