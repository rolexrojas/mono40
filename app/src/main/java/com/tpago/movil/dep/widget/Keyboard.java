package com.tpago.movil.dep.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public final class Keyboard {

  private static InputMethodManager getInputMethodManager(View view) {
    return (InputMethodManager) view.getContext()
      .getSystemService(Context.INPUT_METHOD_SERVICE);
  }

  public static void show(View view) {
    ObjectHelper.checkNotNull(view, "view");
    view.requestFocus();
    final InputMethodManager imm = getInputMethodManager(view);
    if (ObjectHelper.isNotNull(imm)) {
      imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
  }

  public static void hide(View view) {
    ObjectHelper.checkNotNull(view, "view");
    final InputMethodManager imm = getInputMethodManager(view);
    if (ObjectHelper.isNotNull(imm)) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
  }

  public static void hide(Activity activity) {
    ObjectHelper.checkNotNull(activity, "activity");
    hide(activity.getCurrentFocus());
  }

  public static void hide(Fragment fragment) {
    ObjectHelper.checkNotNull(fragment, "fragment");
    hide(fragment.getActivity());
  }

  private Keyboard() {
    throw new AssertionError("Cannot be instantiated");
  }
}
