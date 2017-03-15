package com.tpago.movil.widget;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class Keyboard {
  private static InputMethodManager getInputMethodManager(View view) {
    return (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
  }

  public static void show(View view) {
    Preconditions.checkNotNull(view, "view == null");
    view.requestFocus();
    final InputMethodManager imm = getInputMethodManager(view);
    if (Objects.isNotNull(imm)) {
      imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
  }

  public static void hide(View view) {
    Preconditions.checkNotNull(view, "view == null");
    final InputMethodManager imm = getInputMethodManager(view);
    if (Objects.isNotNull(imm)) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
  }

  private Keyboard() {
    throw new AssertionError("Cannot be instantiated");
  }
}
