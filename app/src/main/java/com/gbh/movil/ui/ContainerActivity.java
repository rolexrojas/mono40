package com.gbh.movil.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.gbh.movil.R;
import com.gbh.movil.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class ContainerActivity<C> extends BaseActivity implements Container<C> {
  /**
   * TODO
   */
  private static final String TAG_SUB_SCREEN = "subScreen";

  @Override
  public void setSubScreen(@NonNull SubFragment<? extends Container<C>> fragment) {
    final FragmentManager manager = getSupportFragmentManager();
    final FragmentTransaction transaction = manager.beginTransaction()
      .setCustomAnimations(R.anim.fragment_transition_enter_sibling,
        R.anim.fragment_transition_exit_sibling, R.anim.fragment_transition_enter_sibling,
        R.anim.fragment_transition_exit_sibling)
      .replace(R.id.container_content, fragment, TAG_SUB_SCREEN);
    if (Utils.isNotNull(manager.findFragmentByTag(TAG_SUB_SCREEN))) {
      transaction.addToBackStack(null);
    }
    transaction.commit();
  }
}
