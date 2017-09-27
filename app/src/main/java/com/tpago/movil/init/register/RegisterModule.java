package com.tpago.movil.init.register;

import com.tpago.movil.R;
import com.tpago.movil.app.FragmentQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.app.FragmentScope;
import com.tpago.movil.init.InitData;
import com.tpago.movil.util.Preconditions;

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
