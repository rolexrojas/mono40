package com.tpago.movil.app.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
