package com.tpago.movil.dep.domain;

import com.tpago.movil.dep.domain.util.Event;
import com.tpago.movil.dep.domain.util.EventType;

/**
 * @author hecvasro
 */
public final class ResetEvent extends Event {
  public ResetEvent() {
    super(EventType.SESSION_RESETTING, false);
  }
}
