package com.tpago.movil.app.ui;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;

import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * {@link Module} that contains providers for objects that related to {@link
 * android.support.v4.app.Fragment fragments} and belong to the {@link android.content.Context
 * context} of an {@link android.app.Activity}.
 *
 * @author hecvasro
 */
@Module
public final class FragmentActivityModule {

  public static FragmentActivityModule create(
    FragmentManager fragmentManager,
    int viewContainerId
  ) {
    return new FragmentActivityModule(fragmentManager, viewContainerId);
  }

  private final FragmentManager fragmentManager;
  private final int viewContainerId;

  private FragmentActivityModule(FragmentManager fragmentManager, @IdRes int viewContainerId) {
    this.fragmentManager = ObjectHelper.checkNotNull(fragmentManager, "manager");
    this.viewContainerId = viewContainerId;
  }

  @Provides
  @ActivityScope
  @ActivityQualifier
  FragmentReplacer fragmentReplacer() {
    return FragmentReplacer.create(this.fragmentManager, this.viewContainerId);
  }
}
