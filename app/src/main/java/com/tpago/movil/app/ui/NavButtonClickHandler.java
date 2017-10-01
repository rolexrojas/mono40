package com.tpago.movil.app.ui;

import android.support.annotation.Nullable;

import com.tpago.movil.util.ObjectHelper;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author hecvasro
 */
public final class NavButtonClickHandler implements OnNavButtonClickedListener {

  static NavButtonClickHandler create() {
    return new NavButtonClickHandler();
  }

  private final Deque<OnNavButtonClickedListener> listenerDeque;

  private NavButtonClickHandler() {
    this.listenerDeque = new ArrayDeque<>();
  }

  public final void push(OnNavButtonClickedListener listener) {
    this.listenerDeque.push(ObjectHelper.checkNotNull(listener, "listener"));
  }

  @Nullable
  public final OnNavButtonClickedListener pop() {
    return !this.listenerDeque.isEmpty() ? this.listenerDeque.pop() : null;
  }

  @Override
  public boolean onNavButtonClicked() {
    for (OnNavButtonClickedListener listener : this.listenerDeque) {
      if (listener.onNavButtonClicked()) {
        return true;
      }
    }
    return false;
  }
}
