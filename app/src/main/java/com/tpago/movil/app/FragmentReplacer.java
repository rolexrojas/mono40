package com.tpago.movil.app;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tpago.movil.R;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
public final class FragmentReplacer {

  public static FragmentReplacer create(FragmentManager fragmentManager, int viewContainerId) {
    return new FragmentReplacer(fragmentManager, viewContainerId);
  }

  private final FragmentManager manager;
  private final int viewContainerId;

  private FragmentReplacer(FragmentManager manager, @IdRes int viewContainerId) {
    this.manager = ObjectHelper.checkNotNull(manager, "manager");
    this.viewContainerId = viewContainerId;
  }

  public final FragmentManager manager() {
    return this.manager;
  }

  public final Transaction begin(Fragment fragment, String tag) {
    return new Transaction(this.manager, this.viewContainerId, fragment, tag);
  }

  public final Transaction begin(Fragment fragment) {
    return this.begin(fragment, null);
  }

  public enum Transition {
    /**
     * Enters and exits by fading in and out respectively.
     * <p>
     * When popped from the back stack, enters and exists by fading in and out respectively.
     */
    FIFO,
    /**
     * Enters by sliding from the right and exists by fading out.
     * <p>
     * When popped from the back stack, enters by fading in  and exits by sliding from the left.
     */
    SRFO
  }

  public static final class Transaction {

    private final FragmentManager manager;
    private final int viewContainerId;
    private final Fragment fragment;
    private final String tag;

    private boolean shouldAddToBackStack = false;
    private String backStackTag;

    private Transition transition;

    private Transaction(
      FragmentManager manager,
      @IdRes int viewContainerId,
      Fragment fragment,
      String tag
    ) {
      this.manager = manager;
      this.viewContainerId = viewContainerId;

      this.fragment = ObjectHelper.checkNotNull(fragment, "fragment");
      this.tag = StringHelper.nullIfEmpty(tag);
    }

    public final Transaction addToBackStack(boolean shouldAddToBackStack, String tag) {
      this.shouldAddToBackStack = shouldAddToBackStack;
      this.backStackTag = StringHelper.nullIfEmpty(tag);
      return this;
    }

    public final Transaction addToBackStack(boolean shouldAddToBackStack) {
      return this.addToBackStack(shouldAddToBackStack, null);
    }

    public final Transaction addToBackStack(String tag) {
      return this.addToBackStack(true, tag);
    }

    public final Transaction addToBackStack() {
      return this.addToBackStack(true);
    }

    public final Transaction transition(Transition transition) {
      this.transition = ObjectHelper.checkNotNull(transition, "transition");
      return this;
    }

    public final void commit() {
      // Begins the transaction.
      final FragmentTransaction transaction = this.manager.beginTransaction();

      // Applies the given transition animation.
      if (ObjectHelper.isNotNull(this.transition)) {
        @AnimRes final int enterAnimId;
        @AnimRes final int exitAnimId;
        @AnimRes final int popEnterAnimId;
        @AnimRes final int popExitAnimId;

        switch (this.transition) {
          case SRFO:
            enterAnimId = R.anim.slide_right_to_left;
            exitAnimId = R.anim.fade_out;
            popEnterAnimId = R.anim.fade_in;
            popExitAnimId = R.anim.slide_left_to_right;
            break;
          default:
            enterAnimId = R.anim.fade_in;
            exitAnimId = R.anim.fade_out;
            popEnterAnimId = R.anim.fade_in;
            popExitAnimId = R.anim.fade_out;
            break;
        }

        transaction.setCustomAnimations(enterAnimId, exitAnimId, popEnterAnimId, popExitAnimId);
      }

      // Replaces the current visible (if any) with the given fragment.
      transaction.replace(this.viewContainerId, this.fragment, this.tag);

      // Adds the transaction to the back stack, if required.
      if (this.shouldAddToBackStack) {
        transaction.addToBackStack(this.backStackTag);
      }

      // Commits the transaction.
      transaction.commit();
    }
  }
}
