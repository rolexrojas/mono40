package com.mono40.movil.dep.init;

import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.app.ui.fragment.base.BaseFragmentModule;
import com.mono40.movil.app.ui.init.unlock.CodeUnlockFragment;
import com.mono40.movil.app.ui.init.unlock.EmailPasswordFragment;
import com.mono40.movil.app.ui.init.unlock.FingerprintUnlockFragment;
import com.mono40.movil.app.ui.init.unlock.PasswordUnlockFragment;
import com.mono40.movil.app.ui.init.unlock.UnlockFragment;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.main.settings.primaryPaymentMethod.PrimaryPaymentMethodFragment;
import com.mono40.movil.dep.init.capture.CaptureComponent;
import com.mono40.movil.dep.init.intro.IntroFragment;
import com.mono40.movil.dep.init.tutorial.TutorialFragment;
import com.mono40.movil.dep.init.register.RegisterComponent;
import com.mono40.movil.dep.init.signin.SignInFragment;
import com.mono40.movil.dep.init.signin.SignInPresenter;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@Deprecated
@ActivityScope
@Subcomponent(modules = {
  ActivityModule.class,
  InitModule.class,
  com.mono40.movil.dep.ActivityModule.class
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
