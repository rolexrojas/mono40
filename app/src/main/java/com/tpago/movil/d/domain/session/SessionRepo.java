package com.tpago.movil.d.domain.session;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public interface SessionRepo {
  /**
   * TODO
   *
   * @return TODO
   */
  Session getSession();

  /**
   * TODO
   *
   * @return TODO
   */
  boolean hasSession();

  /**
   * TODO
   */
  void clearSession();

  /**
   * TODO
   *
   * @param session
   *   TODO
   */
  void setSession(@NonNull Session session);
}
