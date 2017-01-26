package com.tpago.movil.domain;

import com.tpago.movil.domain.util.Event;
import com.tpago.movil.domain.util.EventType;

/**
 * @author hecvasro
 */
public final class ResetEvent extends Event {
  public ResetEvent() {
    super(EventType.SESSION_RESETTING, false);
  }
}
