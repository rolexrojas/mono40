package com.tpago.movil.init.register;

import com.tpago.movil.content.StringResolver;

/**
 * @author hecvasro
 */
final class AvatarRegisterFormPresenter
  extends RegisterFormPresenter<AvatarRegisterFormPresenter.View> {
  AvatarRegisterFormPresenter(
    View view,
    StringResolver stringResolver,
    RegisterData registerData) {
    super(view, stringResolver, registerData);
  }

  @Override
  void onMoveToNextScreenButtonClicked() {
  }

  interface View extends RegisterFormPresenter.View {
  }
}
