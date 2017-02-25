package com.tpago.movil.init.register;

import com.tpago.movil.R;
import com.tpago.movil.app.FragmentQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.app.FragmentScope;
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
    this.fragment = Preconditions.checkNotNull(fragment, "fragment == null");
  }

  @Provides
  @FragmentScope
  RegisterData provideRegisterData() {
    return new RegisterData();
  }

  @Provides
  @FragmentScope
  @FragmentQualifier
  FragmentReplacer provideFragmentReplacer() {
    return new FragmentReplacer(fragment.getChildFragmentManager(), R.id.view_container);
  }
}
