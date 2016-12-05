package com.gbh.movil.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.gbh.movil.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class ChildFragment<T extends Container<?>> extends Fragment {
  /**
   * TODO
   */
  private T container;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  protected final T getContainer() {
    return container;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void onAttach(Context context) {
    super.onAttach(context);
    // Attaches the container to the fragment.
    final Fragment fragment = getParentFragment();
    if (Utils.isNotNull(fragment)) {
      if (!(fragment instanceof Container<?>)) {
        throw new ClassCastException("Parent must implement the 'Container' interface");
      } else {
        container = (T) fragment;
      }
    } else {
      final Activity activity = getActivity();
      if (!(activity instanceof Container<?>)) {
        throw new ClassCastException("Parent must implement the 'Container' interface");
      } else {
        container = (T) activity;
      }
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    // Detaches the parent screen from the fragment.
    container = null;
  }
}
