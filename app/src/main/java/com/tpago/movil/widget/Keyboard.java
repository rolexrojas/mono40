package com.tpago.movil.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static com.tpago.movil.util.Objects.checkIfNotNull;
import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
public final class Keyboard {
  private static InputMethodManager getInputMethodManager(View view) {
    return (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
  }

  public static void show(View view) {
    assertNotNull(view, "view == null");
    view.requestFocus();
    final InputMethodManager imm = getInputMethodManager(view);
    if (checkIfNotNull(imm)) {
      imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
  }

  public static void hide(View view) {
    assertNotNull(view, "view == null");
    final InputMethodManager imm = getInputMethodManager(view);
    if (checkIfNotNull(imm)) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
  }

  public static void hide(Activity activity) {
    assertNotNull(activity, "activit == null");
    hide(activity.getCurrentFocus());
  }

  public static void hide(Fragment fragment) {
    assertNotNull(fragment, "fragment == null");
    hide(fragment.getActivity());
  }

  private Keyboard() {
    throw new AssertionError("Cannot be instantiated");
  }
}
