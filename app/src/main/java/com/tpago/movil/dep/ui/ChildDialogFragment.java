package com.tpago.movil.dep.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class ChildDialogFragment<T extends Container<?>> extends DialogFragment
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
