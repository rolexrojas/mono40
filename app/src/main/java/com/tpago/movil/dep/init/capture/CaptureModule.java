package com.tpago.movil.dep.init.capture;

import android.support.v4.app.FragmentManager;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.fragment.FragmentQualifier;
import com.tpago.movil.app.ui.fragment.FragmentReplacer;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.dep.init.InitData;

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
