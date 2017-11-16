package com.tpago.movil.d.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
public abstract class SwitchableContainerActivity<C> extends ContainerActivity<C> implements
  SwitchableContainer<C> {

  private static final String TAG_CHILD_FRAGMENT = "childFragment";

  @Override
  public void setChildFragment(ChildFragment<? extends Container<C>> f, boolean bs, boolean a) {
    final FragmentManager fm = getSupportFragmentManager();
    final FragmentTransaction ft = fm.beginTransaction();

    if (a) {
      ft.setCustomAnimations(
        R.anim.fragment_transition_enter_sibling,
        R.anim.fragment_transition_exit_sibling,
        R.anim.fragment_transition_enter_sibling,
        R.anim.fragment_transition_exit_sibling
      );
    }

    ft.replace(R.id.containerFrameLayout, f, TAG_CHILD_FRAGMENT);

    if (bs) {
      ft.addToBackStack(null);
    }

    ft.commit();
  }
}
