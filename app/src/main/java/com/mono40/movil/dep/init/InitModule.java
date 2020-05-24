package com.mono40.movil.dep.init;

import com.mono40.movil.R;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.dep.ActivityBase;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;

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
  @ActivityQualifier
  FragmentReplacer provideFragmentReplacer(ActivityBase activity) {
    return FragmentReplacer.create(activity.getSupportFragmentManager(), R.id.view_container);
  }

  @Provides
  @ActivityScope
  LogoAnimator provideLogoAnimator(ActivityBase activity) {
    return new LogoAnimator(
      ((InitActivityBase) activity).logo,
      ((InitActivityBase) activity).placeholderView,
      activity.getResources()
        .getInteger(R.integer.anim_duration_test)
    );
  }
}
