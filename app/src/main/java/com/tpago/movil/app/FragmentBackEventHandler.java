package com.tpago.movil.app;

import android.support.v4.app.FragmentManager;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public class FragmentBackEventHandler implements OnBackPressedListener {
  private final FragmentManager fragmentManager;

  public FragmentBackEventHandler(FragmentManager fragmentManager) {
    this.fragmentManager = Preconditions.checkNotNull(fragmentManager, "fragmentManager == null");
  }

  @Override
  public boolean onBackPressed() {
    if (fragmentManager.getBackStackEntryCount() == 0) {
      return false;
    } else {
      fragmentManager.popBackStack();
      return true;
    }
  }
}
