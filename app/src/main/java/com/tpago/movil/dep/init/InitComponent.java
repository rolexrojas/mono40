package com.tpago.movil.dep.init;

import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.fragment.base.BaseFragmentModule;
import com.tpago.movil.app.ui.init.unlock.CodeUnlockFragment;
import com.tpago.movil.app.ui.init.unlock.EmailPasswordFragment;
import com.tpago.movil.app.ui.init.unlock.FingerprintUnlockFragment;
import com.tpago.movil.app.ui.init.unlock.PasswordUnlockFragment;
import com.tpago.movil.app.ui.init.unlock.UnlockFragment;
import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.app.ui.main.settings.primaryPaymentMethod.PrimaryPaymentMethodFragment;
import com.tpago.movil.dep.init.capture.CaptureComponent;
import com.tpago.movil.dep.init.intro.IntroFragment;
import com.tpago.movil.dep.init.tutorial.TutorialFragment;
import com.tpago.movil.dep.init.register.RegisterComponent;
import com.tpago.movil.dep.init.signin.SignInFragment;
import com.tpago.movil.dep.init.signin.SignInPresenter;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@Deprecated
@ActivityScope
@Subcomponent(modules = {
  ActivityModule.class,
  InitModule.class,
  com.tpago.movil.dep.ActivityModule.class
})
public interface InitComponent {

  RegisterComponent plus(BaseFragmentModule module);

  CaptureComponent capture(BaseFragmentModule module);

  void inject(InitActivityBase activity);

  void inject(InitFragment fragment);

  void inject(PhoneNumberInitFragment fragment);

  void inject(PhoneNumberInitPresenter presenter);

  void inject(IntroFragment fragment);

  void inject(SignInFragment fragment);

  void inject(SignInPresenter presenter);

  void inject(TutorialFragment fragment);

  void inject(UnlockFragment fragment);

  void inject(CodeUnlockFragment fragment);

  void inject(FingerprintUnlockFragment fragment);

  void inject(PasswordUnlockFragment fragment);

  void inject(PrimaryPaymentMethodFragment fragment);

  void inject(EmailPasswordFragment fragment);

  void inject(OneTimePasswordPresenter presenter);

  void inject(OneTimePasswordFragment fragment);
}
