package com.mono40.movil.d.ui;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mono40.movil.util.ObjectHelper;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ChildFragmentHelper<T extends Container<?>> implements ContainerChild<T> {

  /**
   * TODO
   */
  private final T container;

  /**
   * TODO
   *
   * @param parentFragment
   *   TODO
   * @param parentActivity
   *   TODO
   *
   * @throws ClassCastException
   *   TODO
   */
  @SuppressWarnings("unchecked")
  private ChildFragmentHelper(@Nullable Fragment parentFragment, @NonNull Activity parentActivity)
    throws ClassCastException {
    if (ObjectHelper.isNotNull(parentFragment)) {
      if (!(parentFragment instanceof Container<?>)) {
        throw new ClassCastException("Parent fragment must implement the 'Container' interface");
      } else {
        container = (T) parentFragment;
      }
    } else {
      if (!(parentActivity instanceof Container<?>)) {
        throw new ClassCastException("Parent activity must implement the 'Container' interface");
      } else {
        container = (T) parentActivity;
      }
    }
  }

  /**
   * TODO
   *
   * @param parentFragment
   *   TODO
   * @param parentActivity
   *   TODO
   * @param <T>
   *   TODO
   *
   * @return TODO
   *
   * @throws ClassCastException
   *   TODO
   */
  @NonNull
  public static <T extends Container<?>> ChildFragmentHelper<T> attach(
    @Nullable Fragment parentFragment, @NonNull Activity parentActivity
  ) throws ClassCastException {
    return new ChildFragmentHelper<>(parentFragment, parentActivity);
  }

  @NonNull
  @Override
  public T getContainer() {
    return container;
  }
}
