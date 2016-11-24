package com.gbh.movil.ui;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.gbh.movil.R;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class ContainerActivity<C> extends BaseActivity implements Container<C> {
  /**
   * TODO
   *
   * @param fragment
   *   TODO
   * @param addToBackStack
   *   TODO
   * @param animateTransition
   *   TODO
   */
  protected final void replaceFragment(@NonNull Fragment fragment, boolean addToBackStack,
    boolean animateTransition) {
    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    if (animateTransition) {
      transaction.setCustomAnimations(R.anim.fragment_transition_enter_sibling,
        R.anim.fragment_transition_exit_sibling, R.anim.fragment_transition_enter_sibling,
        R.anim.fragment_transition_exit_sibling);
    }
    transaction.replace(getContainerId(), fragment);
    if (addToBackStack) {
      transaction.addToBackStack(null);
    }
    transaction.commit();
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @IdRes
  protected abstract int getContainerId();

  @Override
  public void setSubScreen(@NonNull SubFragment<? extends Container<C>> fragment) {
    replaceFragment(fragment, true, true);
  }
}
