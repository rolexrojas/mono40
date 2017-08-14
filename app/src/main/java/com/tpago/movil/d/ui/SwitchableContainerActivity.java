package com.tpago.movil.d.ui;

import static com.tpago.movil.util.Objects.checkIfNull;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
public abstract class SwitchableContainerActivity<C> extends ContainerActivity<C> implements SwitchableContainer<C> {
  private static final String TAG_CHILD_FRAGMENT = "childFragment";

  protected void setChildFragment(ChildFragment<? extends Container<C>> f, String t, boolean bs, boolean a) {
    final String tag = t == null || t.isEmpty() ? TAG_CHILD_FRAGMENT : t;

    final FragmentManager fm = getSupportFragmentManager();
    final Fragment cf = fm.findFragmentByTag(tag);
    if (checkIfNull(cf) || !cf.getClass().isAssignableFrom(f.getClass())) {
      final FragmentTransaction ft = fm.beginTransaction();

      if (a) {
        ft.setCustomAnimations(
          R.anim.fragment_transition_enter_sibling,
          R.anim.fragment_transition_exit_sibling,
          R.anim.fragment_transition_enter_sibling,
          R.anim.fragment_transition_exit_sibling
        );
      }

      ft.replace(R.id.container, f, tag);

      if (bs) {
        ft.addToBackStack(null);
      }

      ft.commit();
    }
  }

  @Override
  public void setChildFragment(ChildFragment<? extends Container<C>> f, boolean bs, boolean a) {
    this.setChildFragment(f, TAG_CHILD_FRAGMENT, bs, a);
  }
}
