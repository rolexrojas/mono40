package com.tpago.movil.dep.widget;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public final class FullSizeLoadIndicator implements LoadIndicator {

  private static final String TAG = FullSizeLoadIndicator.class.getSimpleName();

  private final FragmentManager fragmentManager;

  public FullSizeLoadIndicator(FragmentManager fragmentManager) {
    this.fragmentManager = ObjectHelper.checkNotNull(fragmentManager, "fragmentManager");
  }

  @Override
  public void start() {
    final Fragment fragment = fragmentManager.findFragmentByTag(TAG);
    if (ObjectHelper.isNull(fragment)) {
      FullSizeLoadIndicatorDialogFragment.create()
        .show(fragmentManager, TAG);
    }
  }

  @Override
  public void stop() {
    final Fragment fragment = fragmentManager.findFragmentByTag(TAG);
    if (ObjectHelper.isNotNull(fragment) && fragment instanceof FullSizeLoadIndicatorDialogFragment) {
      ((DialogFragment) fragment).dismiss();
    }
  }

  public static final class FullSizeLoadIndicatorDialogFragment extends DialogFragment {

    static FullSizeLoadIndicatorDialogFragment create() {
      return new FullSizeLoadIndicatorDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setStyle(STYLE_NO_FRAME, R.style.D_Widget_FullSizeLoadIndicator_Light);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
      return inflater.inflate(R.layout.widget_load_indicator_full_size, container, false);
    }
  }
}
