package com.mono40.movil.app.ui.activity.fragment;

import androidx.fragment.app.FragmentManager;

import com.mono40.movil.R;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;

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
