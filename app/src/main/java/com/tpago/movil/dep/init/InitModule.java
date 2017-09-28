package com.tpago.movil.dep.init;

import com.tpago.movil.R;
import com.tpago.movil.dep.Session;
import com.tpago.movil.app.ui.ActivityQualifier;
import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.dep.BaseActivity;
import com.tpago.movil.app.ui.FragmentReplacer;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class InitModule {
  @Provides
  @ActivityScope
  InitData provideInitData() {
    return new InitData();
  }

  @Provides
  @ActivityScope
  Session.Builder provideSessionBuilder() {
    return new Session.Builder();
  }

  @Provides
  @ActivityScope
  @ActivityQualifier
  FragmentReplacer provideFragmentReplacer(BaseActivity activity) {
    return FragmentReplacer.create(activity.getSupportFragmentManager(), R.id.view_container);
  }

  @Provides
  @ActivityScope
  LogoAnimator provideLogoAnimator(BaseActivity activity) {
    return new LogoAnimator(
      ((InitActivity) activity).logo,
      ((InitActivity) activity).placeholderView,
      activity.getResources().getInteger(R.integer.anim_duration_test));
  }
}
