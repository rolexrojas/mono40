package com.mono40.movil.app.ui.fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mono40.movil.util.ObjectHelper;

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
