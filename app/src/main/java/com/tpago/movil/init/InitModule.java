package com.tpago.movil.init;

import com.tpago.movil.R;
import com.tpago.movil.Session;
import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.app.BaseActivity;
import com.tpago.movil.app.FragmentReplacer;

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
