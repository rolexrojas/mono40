package com.tpago.movil.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
public final class Fragments {
  public static void replace(
    FragmentManager manager,
    Fragment fragment,
    Transition transition,
    boolean addToBackStack) {
    final FragmentTransaction transaction = manager.beginTransaction();
    switch (transition) {
      case SRFO:
        transaction.setCustomAnimations(
          R.anim.enter_slide_right,
          R.anim.exit_fade,
          R.anim.enter_fade,
          R.anim.exit_slide_right);
        break;
    }
    transaction.replace(R.id.view_container, fragment);
    if (addToBackStack) {
      transaction.addToBackStack(null);
    }
    transaction.commit();
  }

  public static void replace(FragmentManager manager, Fragment fragment) {
    replace(manager, fragment, Transition.NONE, false);
  }

  private Fragments() {
    throw new AssertionError("Cannot be instantiated");
  }

  public enum Transition {
    NONE,
    /**
     * Enters and exits sliding from and to the right. Previous fragment is faded out while exiting
     * and faded in while entering.
     */
    SRFO
  }
}
