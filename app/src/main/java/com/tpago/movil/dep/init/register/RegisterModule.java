package com.tpago.movil.dep.init.register;

import android.support.v4.app.FragmentManager;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.FragmentQualifier;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.dep.init.InitData;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class RegisterModule {

  @Provides
  @FragmentScope
  @FragmentQualifier
  FragmentReplacer provideFragmentReplacer(@FragmentQualifier FragmentManager fragmentManager) {
    return FragmentReplacer.create(fragmentManager, R.id.view_container);
  }

  @Provides
  @FragmentScope
  RegisterData provideRegisterData(InitData initData) {
    return new RegisterData(initData);
  }
}
