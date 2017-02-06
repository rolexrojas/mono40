package com.tpago.movil.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tpago.movil.R;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class SwitchableContainerActivity<C> extends ContainerActivity<C>
  implements SwitchableContainer<C> {
  /**
   * TODO
   */
  private static final String TAG_CHILD_FRAGMENT = "childFragment";

  @Override
  public void setChildFragment(@NonNull ChildFragment<? extends Container<C>> fragment,
    boolean addToBackStack, boolean animate) {
    final FragmentManager manager = getSupportFragmentManager();
    final FragmentTransaction transaction = manager.beginTransaction();
    if (animate) {
      transaction.setCustomAnimations(R.anim.fragment_transition_enter_sibling,
        R.anim.fragment_transition_exit_sibling, R.anim.fragment_transition_enter_sibling,
        R.anim.fragment_transition_exit_sibling);
    }
    transaction.replace(R.id.container, fragment, TAG_CHILD_FRAGMENT);
    if (addToBackStack) {
      transaction.addToBackStack(null);
    }
    transaction.commit();
  }
}
