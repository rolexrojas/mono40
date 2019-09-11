package com.tpago.movil.app.ui.activity;

import androidx.annotation.Nullable;

import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author hecvasro
 */
public final class NavButtonPressEventHandler implements NavButtonPressEventConsumer {

  public static NavButtonPressEventHandler create() {
    return new NavButtonPressEventHandler();
  }

  private final Deque<NavButtonPressEventConsumer> consumers;

  private NavButtonPressEventHandler() {
    this.consumers = new ArrayDeque<>();
  }

  public final void push(NavButtonPressEventConsumer consumer) {
    this.consumers.push(ObjectHelper.checkNotNull(consumer, "consumer"));
  }

  @Nullable
  public final NavButtonPressEventConsumer pop() {
    if (this.consumers.isEmpty()) {
      return null;
    }
    return this.consumers.pop();
  }

  @Override
  public boolean accept() {
    for (NavButtonPressEventConsumer consumer : this.consumers) {
      if (consumer.accept()) {
        return true;
      }
    }
    return false;
  }
}
