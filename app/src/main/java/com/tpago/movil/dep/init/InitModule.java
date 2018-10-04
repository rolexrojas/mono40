package com.tpago.movil.dep.init;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.dep.ActivityBase;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;

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
