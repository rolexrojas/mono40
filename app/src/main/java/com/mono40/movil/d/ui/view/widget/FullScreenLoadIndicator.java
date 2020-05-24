package com.mono40.movil.d.ui.view.widget;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mono40.movil.R;
import com.mono40.movil.util.ObjectHelper;

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
    if (ObjectHelper.isNull(fragment)) {
      FullScreenLoadIndicatorDialogFragment.newInstance()
        .show(fragmentManager, TAG);
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
    public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState
    ) {
      return inflater.inflate(R.layout.d_full_screen_load_indicator, container, false);
    }
  }
}
