package com.mono40.movil.dep.init.capture;

import androidx.fragment.app.FragmentManager;

import com.mono40.movil.R;
import com.mono40.movil.app.ui.fragment.FragmentQualifier;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;
import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.dep.init.InitData;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class CaptureModule {

  @Provides
  @FragmentScope
  @FragmentQualifier
  FragmentReplacer provideFragmentReplacer(@FragmentQualifier FragmentManager fragmentManager) {
    return FragmentReplacer.create(fragmentManager, R.id.view_container);
  }

  @Provides
  @FragmentScope
  CaptureData provideCaptureData(InitData initData) {
    return new CaptureData(initData);
  }
}
