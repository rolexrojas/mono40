package com.tpago.movil.dep;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Workaround for the following issues:
 * <ul>
 * <li><a href="https://code.google.com/p/android/issues/detail?id=40323">https://code.google.com/p/android/issues/detail?id=40323</a></li>
 * <li><a href="https://code.google.com/p/android/issues/detail?can=2&start=0&num=100&q=Awful.vi%40gmail.com&colspec=ID%20Status%20Priority%20Owner%20Summary%20Stars%20Reporter%20Opened&groupby=&sort=&id=225100">https://code.google.com/p/android/issues/detail?can=2&start=0&num=100&q=Awful.vi%40gmail.com&colspec=ID%20Status%20Priority%20Owner%20Summary%20Stars%20Reporter%20Opened&groupby=&sort=&id=225100</a></li>
 * </ul>
 *
 * @author hecvasro
 */
@Deprecated
public final class BackEventHandler implements OnBackPressedListener {
  private final Deque<OnBackPressedListener> listeners;

  public BackEventHandler() {
    listeners = new ArrayDeque<>();
  }

  public final void push(OnBackPressedListener listener) {
    listeners.push(listener);
  }

  public final void remove(OnBackPressedListener listener) {
    listeners.remove(listener);
  }

  @Override
  public boolean onBackPressed() {
    for (OnBackPressedListener listener : listeners) {
      if (listener.onBackPressed()) {
        return true;
      }
    }
    return false;
  }
}
