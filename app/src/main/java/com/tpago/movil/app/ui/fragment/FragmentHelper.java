package com.tpago.movil.app.ui.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author Hector Vasquez
 */
public final class FragmentHelper {

  @Nullable
  public static Fragment findByTag(FragmentManager fragmentManager, String tag) {
    ObjectHelper.checkNotNull(fragmentManager, "manager");
    ObjectHelper.checkNotNull(tag, "tag");

    return fragmentManager.findFragmentByTag(tag);
  }

  public static void dismissByTag(FragmentManager fragmentManager, String tag) {
    final Fragment fragment = findByTag(fragmentManager, tag);
    if (ObjectHelper.isNotNull(fragment)) {
      if (fragment instanceof DialogFragment) {
        ((DialogFragment) fragment).dismiss();
      } else {
        fragmentManager.beginTransaction()
                .remove(fragment)
                .commit();
      }
    }
  }

  private FragmentHelper() {
  }
}
