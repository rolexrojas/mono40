package com.tpago.movil.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tpago.movil.R;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public final class FragmentReplacer {
  private final FragmentManager fragmentManager;
  private final int viewContainerId;

  public FragmentReplacer(FragmentManager fragmentManager, int viewContainerId) {
    this.fragmentManager = Preconditions.assertNotNull(fragmentManager, "fragmentManager == null");
    this.viewContainerId = viewContainerId;
  }

  public final Transaction begin(Fragment fragment) {
    return new Transaction(fragmentManager, viewContainerId, fragment);
  }

  public enum Transition {
    NONE,
    FIFO,
    /**
     * Enters and exits sliding from and to the right. Previous fragment is faded out while exiting
     * and faded in while entering.
     */
    SRFO
  }

  public static final class Transaction {
    private final FragmentManager fragmentManager;
    private final int viewContainerId;

    private final Fragment fragment;

    private String backStackTag = null;
    private boolean addToBackStack = false;

    private Transition transition = Transition.NONE;

    private Transaction(FragmentManager fragmentManager, int viewContainerId, Fragment fragment) {
      this.fragmentManager = Preconditions.assertNotNull(fragmentManager, "fragmentManager == null");
      this.viewContainerId = viewContainerId;
      this.fragment = Preconditions.assertNotNull(fragment, "fragment == null");
    }

    public final Transaction addToBackStack() {
      addToBackStack = true;
      return this;
    }

    public final Transaction addToBackStack(String tag) {
      backStackTag = tag;
      addToBackStack = true;
      return this;
    }

    public final Transaction setTransition(Transition transition) {
      if (Objects.checkIfNotNull(transition)) {
        this.transition = transition;
      }
      return this;
    }

    public final void commit() {
      // Begins the transaction.
      final FragmentTransaction transaction = fragmentManager.beginTransaction();
      // Applies the given transition animation.
      switch (transition) {
        case FIFO:
          transaction.setCustomAnimations(
            R.anim.enter_fade,
            R.anim.exit_fade,
            R.anim.enter_fade,
            R.anim.exit_fade);
        case SRFO:
          transaction.setCustomAnimations(
            R.anim.enter_slide_right,
            R.anim.exit_fade,
            R.anim.enter_fade,
            R.anim.exit_slide_right);
          break;
      }
      // Replaces the current one with the given one.
      transaction.replace(viewContainerId, fragment);
      // Adds the transaction to the back stack, if requested.
      if (addToBackStack) {
        transaction.addToBackStack(backStackTag);
      }
      // Commits the transaction.
      transaction.commit();
    }
  }
}
