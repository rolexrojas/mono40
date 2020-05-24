package com.mono40.movil.d.domain;

import com.mono40.movil.d.domain.util.Event;
import com.mono40.movil.d.domain.util.EventType;

/**
 * @author hecvasro
 */
@Deprecated
public final class ResetEvent extends Event {
  public ResetEvent() {
    super(EventType.SESSION_RESETTING, false);
  }
}
