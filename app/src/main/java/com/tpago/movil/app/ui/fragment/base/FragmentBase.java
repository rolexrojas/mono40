package com.tpago.movil.app.ui.fragment.base;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Hector Vasquez
 */
public abstract class FragmentBase extends Fragment {

  private Unbinder unbinder;

  /**
   * Layout resource identifier of the fragment
   */
  @LayoutRes
  protected abstract int layoutResId();

  @Override
  public View onCreateView(
    @NonNull LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(this.layoutResId(), container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Binds all annotated resources, views, and methods.
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onDestroyView() {
    // Unbinds all annotated resources, views, and methods.
    unbinder.unbind();

    super.onDestroyView();
  }
}
