package com.mono40.movil.dep;

import androidx.fragment.app.FragmentManager;

import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public class FragmentBackEventHandler implements OnBackPressedListener {

  private final FragmentManager fragmentManager;

  public FragmentBackEventHandler(FragmentManager fragmentManager) {
    this.fragmentManager = ObjectHelper.checkNotNull(fragmentManager, "fragmentManager");
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
