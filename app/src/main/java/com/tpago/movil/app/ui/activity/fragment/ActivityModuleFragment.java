package com.tpago.movil.app.ui.activity.fragment;

import android.support.v4.app.FragmentManager;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.activity.ActivityQualifier;
import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class ActivityModuleFragment {

  @Provides
  @ActivityScope
  @ActivityQualifier
  FragmentReplacer fragmentReplacer(FragmentManager fragmentManager) {
    return FragmentReplacer.create(fragmentManager, R.id.containerFrameLayout);
  }
}
