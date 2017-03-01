package com.tpago.movil.init;

import com.tpago.movil.R;
import com.tpago.movil.Session;
import com.tpago.movil.app.ActivityModule;
import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.app.BackEventHandler;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class InitModule extends ActivityModule<InitActivity> {
  InitModule(InitActivity activity) {
    super(activity);
  }

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
  BackEventHandler provideBackEventHandler() {
    return new BackEventHandler();
  }

  @Provides
  @ActivityScope
  @ActivityQualifier
  FragmentReplacer provideFragmentReplacer() {
    return new FragmentReplacer(activity.getSupportFragmentManager(), R.id.view_container);
  }

  @Provides
  @ActivityScope
  LogoAnimator provideLogoAnimator() {
    return new LogoAnimator(
      activity.logo,
      activity.placeholderView,
      activity.getResources().getInteger(R.integer.anim_duration_test));
  }
}
