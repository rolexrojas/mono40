package com.tpago.movil.dep.ui.view.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.dep.misc.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public class FullScreenLoadIndicator implements LoadIndicator {
  /**
   * TODO
   */
  private static final String TAG = FullScreenLoadIndicator.class.getSimpleName();

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
  public FullScreenLoadIndicator(@NonNull FragmentManager fragmentManager) {
    this.fragmentManager = fragmentManager;
  }

  @Override
  public void show() {
    final Fragment fragment = fragmentManager.findFragmentByTag(TAG);
    if (Utils.isNull(fragment)) {
      FullScreenLoadIndicatorDialogFragment.newInstance().show(fragmentManager, TAG);
    }
  }

  @Override
  public void hide() {
    final Fragment fragment = fragmentManager.findFragmentByTag(TAG);
    if (fragment instanceof FullScreenLoadIndicatorDialogFragment) {
      ((FullScreenLoadIndicatorDialogFragment) fragment).dismiss();
    }
  }

  /**
   * TODO
   */
  public static class FullScreenLoadIndicatorDialogFragment extends DialogFragment {
    /**
     * TODO
     *
     * @return TODO
     */
    @NonNull
    protected static FullScreenLoadIndicatorDialogFragment newInstance() {
      return new FullScreenLoadIndicatorDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setStyle(STYLE_NO_FRAME, R.style.FullScreenLoadIndicator_Theme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.full_screen_load_indicator, container, false);
    }
  }
}
