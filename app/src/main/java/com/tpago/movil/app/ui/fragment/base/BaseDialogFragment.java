package com.tpago.movil.app.ui.fragment.base;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Hector Vasquez
 */
public abstract class BaseDialogFragment extends DialogFragment {

  private Unbinder unbinder;

  /**
   * Layout resource identifier of the fragment
   */
  @LayoutRes
  protected abstract int layoutResId();

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(this.layoutResId(), container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Binds all annotated resources, views, and methods.
    this.unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onDestroyView() {
    // Unbinds all annotated resources, views, and methods.
    this.unbinder.unbind();

    super.onDestroyView();
  }
}
