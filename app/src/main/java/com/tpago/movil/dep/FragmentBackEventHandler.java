package com.tpago.movil.dep;

import android.support.v4.app.FragmentManager;

/**
 * @author hecvasro
 */
@Deprecated
public class FragmentBackEventHandler implements OnBackPressedListener {
  private final FragmentManager fragmentManager;

  public FragmentBackEventHandler(FragmentManager fragmentManager) {
    this.fragmentManager = Preconditions.assertNotNull(fragmentManager, "fragmentManager == null");
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
