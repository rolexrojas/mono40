package com.tpago.movil.init.register;

import com.tpago.movil.app.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = RegisterModule.class)
public interface RegisterComponent {
  void inject(RegisterFragment fragment);

  void inject(PhoneNumberFormFragment fragment);
  void inject(NameRegisterFormFragment fragment);
  void inject(AvatarFormFragment fragment);
  void inject(EmailRegisterFormFragment fragment);
  void inject(PasswordRegisterFormFragment fragment);
}
