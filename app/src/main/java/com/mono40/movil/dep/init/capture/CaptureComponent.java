package com.mono40.movil.dep.init.capture;

import com.mono40.movil.app.ui.fragment.FragmentScope;
import com.mono40.movil.app.ui.fragment.base.BaseFragmentModule;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(
  modules = {
    BaseFragmentModule.class,
    CaptureModule.class
  }
)
public interface CaptureComponent {

  void inject(CaptureFragment fragment);

  void inject(NameCaptureFormFragment fragment);

  void inject(EmailCaptureFormFragment fragment);

  void inject(BankCaptureFormFragment fragment);
}
