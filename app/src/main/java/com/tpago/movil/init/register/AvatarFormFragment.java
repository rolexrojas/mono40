package com.tpago.movil.init.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tpago.movil.R;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.util.Objects;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public final class AvatarFormFragment
  extends RegisterFormFragment<AvatarRegisterFormPresenter>
  implements AvatarRegisterFormPresenter.View {
  private AvatarRegisterFormPresenter presenter;

  @Inject
  StringResolver stringResolver;
  @Inject
  RegisterData registerData;

  static AvatarFormFragment create() {
    return new AvatarFormFragment();
  }

  @Override
  protected AvatarRegisterFormPresenter getPresenter() {
    if (Objects.isNull(presenter)) {
      presenter = new AvatarRegisterFormPresenter(this, stringResolver, registerData);
    }
    return presenter;
  }

  @Override
  protected Fragment getNextScreen() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    getRegisterComponent().inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_register_form_profile_picture, container, false);
  }
}
