package com.mono40.movil.d.ui;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mono40.movil.R;

/**
 * @author hecvasro
 */
public abstract class SwitchableContainerActivityBase<C> extends ContainerActivityBase<C> implements
  SwitchableContainer<C> {

  protected static final String TAG_CHILD_FRAGMENT = "childFragment";

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
