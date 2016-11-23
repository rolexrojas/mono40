package com.gbh.movil.ui.view.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gbh.movil.Utils;
import com.gbh.movil.ui.FullScreenDialogFragment;

/**
 * TODO
 *
 * @author hecvasro
 */
public class FullScreenRefreshIndicator implements RefreshIndicator {
  /**
   * TODO
   */
  private static final String TAG_REFRESH_INDICATOR = "refreshIndicator";

  /**
   * TODO
   */
  private final FragmentManager fragmentManager;

  /**
   * TODO
   *
   * @param fragmentManager
   *   TODO
   */
  public FullScreenRefreshIndicator(@NonNull FragmentManager fragmentManager) {
    this.fragmentManager = fragmentManager;
  }

  @Override
  public void show() {
    final Fragment fragment = fragmentManager.findFragmentByTag(TAG_REFRESH_INDICATOR);
    if (Utils.isNull(fragment)) {
      RefreshIndicatorFullScreenDialogFragment.newInstance()
        .show(fragmentManager, TAG_REFRESH_INDICATOR);
    }
  }

  @Override
  public void hide() {
    final Fragment fragment = fragmentManager.findFragmentByTag(TAG_REFRESH_INDICATOR);
    if (fragment instanceof RefreshIndicatorFullScreenDialogFragment) {
      ((RefreshIndicatorFullScreenDialogFragment) fragment).dismiss();
    }
  }

  /**
   * TODO
   */
  public static class RefreshIndicatorFullScreenDialogFragment extends FullScreenDialogFragment {
    /**
     * TODO
     *
     * @return TODO
     */
    @NonNull
    protected static RefreshIndicatorFullScreenDialogFragment newInstance() {
      return new RefreshIndicatorFullScreenDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
      return super.onCreateView(inflater, container, savedInstanceState);
    }
  }
}
