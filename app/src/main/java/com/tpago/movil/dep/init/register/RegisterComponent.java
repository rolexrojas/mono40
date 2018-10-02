package com.tpago.movil.dep.init.register;

import com.tpago.movil.app.ui.fragment.base.BaseFragmentModule;
import com.tpago.movil.app.ui.fragment.FragmentScope;
import com.tpago.movil.app.ui.picture.PictureCreatorModule;
import com.tpago.movil.app.ui.picture.PictureCreatorPresentationComponentBuilderModule;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(
  modules = {
    BaseFragmentModule.class,
    PictureCreatorModule.class,
    PictureCreatorPresentationComponentBuilderModule.class,
    RegisterModule.class
  }
)
public interface RegisterComponent {

  void inject(RegisterFragment fragment);

  void inject(NameRegisterFormFragment fragment);

  void inject(AvatarFormFragment fragment);

  void inject(EmailRegisterFormFragment fragment);

  void inject(EmailOTPRegisterFormFragment fragment);

  void inject(EmailOTPRegisterFormPresenter presenter);

  void inject(PasswordRegisterFormFragment fragment);

  void inject(PinRegisterFormFragment fragment);

  void inject(PinRegisterFormPresenter presenter);

  void inject(SummaryRegisterFragment fragment);
}
