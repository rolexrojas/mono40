package com.tpago.movil.dep.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tpago.movil.R;
import com.tpago.movil.util.Objects;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class SwitchableContainerActivity<C> extends ContainerActivity<C>
  implements SwitchableContainer<C> {
  private static final String TAG_CHILD_FRAGMENT = "childFragment";

  @Override
  public void setChildFragment(
    ChildFragment<? extends Container<C>> nf,
    boolean addToBackStack,
    boolean animate) {
    final FragmentManager m = getSupportFragmentManager();
    final Fragment cf = m.findFragmentByTag(TAG_CHILD_FRAGMENT);
    if (Objects.isNull(cf) || !cf.getClass().isAssignableFrom(nf.getClass())) {
      final FragmentTransaction t = m.beginTransaction();
      if (animate) {
        t.setCustomAnimations(
          R.anim.fragment_transition_enter_sibling,
          R.anim.fragment_transition_exit_sibling,
          R.anim.fragment_transition_enter_sibling,
          R.anim.fragment_transition_exit_sibling);
      }
      t.replace(R.id.container, nf, TAG_CHILD_FRAGMENT);
      if (addToBackStack) {
        t.addToBackStack(null);
      }
      t.commit();
    }
  }
}
