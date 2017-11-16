package com.tpago.movil.d.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * @author hecvasro
 */
public abstract class ChildFragment<T extends Container<?>> extends Fragment
  implements ContainerChild<T> {

  private ChildFragmentHelper<T> helper;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    helper = ChildFragmentHelper.attach(getParentFragment(), getActivity());
  }

  @NonNull
  @Override
  public T getContainer() {
    return helper.getContainer();
  }
}
