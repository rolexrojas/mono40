package com.tpago.movil.d.ui;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

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
