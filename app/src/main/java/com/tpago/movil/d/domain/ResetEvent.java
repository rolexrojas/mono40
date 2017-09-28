package com.tpago.movil.d.domain;

import com.tpago.movil.d.domain.util.Event;
import com.tpago.movil.d.domain.util.EventType;

/**
 * @author hecvasro
 */
@Deprecated
public final class ResetEvent extends Event {
  public ResetEvent() {
    super(EventType.SESSION_RESETTING, false);
  }
}
