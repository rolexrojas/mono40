package com.tpago.movil.dep.init.capture;

import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.app.ui.fragment.base.BaseFragmentModule;

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
