package com.gbh.movil.domain;

import com.gbh.movil.domain.util.Event;
import com.gbh.movil.domain.util.EventType;

/**
 * @author hecvasro
 */
public final class ResetEvent extends Event {
  public ResetEvent() {
    super(EventType.SESSION_RESETTING, false);
  }
}
