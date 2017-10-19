package com.tpago.movil.dep.init.register;

import com.tpago.movil.app.ui.FragmentModule;
import com.tpago.movil.app.ui.FragmentScope;
import com.tpago.movil.app.ui.picture.PictureCreatorModule;
import com.tpago.movil.app.ui.picture.PictureCreatorPresentationComponentBuilderModule;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(
  modules = {
    FragmentModule.class,
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

  void inject(PasswordRegisterFormFragment fragment);

  void inject(PinRegisterFormFragment fragment);

  void inject(PinRegisterFormPresenter presenter);

  void inject(SummaryRegisterFragment fragment);
}
