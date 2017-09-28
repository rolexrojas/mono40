package com.tpago.movil.dep.init.register;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.FragmentQualifier;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.dep.init.InitData;
import com.tpago.movil.dep.Preconditions;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class RegisterModule {
  private final RegisterFragment fragment;

  RegisterModule(RegisterFragment fragment) {
    this.fragment = Preconditions.assertNotNull(fragment, "fragment == null");
  }

  @Provides
  @FragmentScope
  RegisterData provideRegisterData(InitData initData) {
    return new RegisterData(initData);
  }

  @Provides
  @FragmentScope
  @FragmentQualifier
  FragmentReplacer provideFragmentReplacer() {
    return FragmentReplacer.create(fragment.getChildFragmentManager(), R.id.view_container);
  }
}
