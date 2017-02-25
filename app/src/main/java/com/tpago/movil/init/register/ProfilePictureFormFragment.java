package com.tpago.movil.init.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tpago.movil.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class ProfilePictureFormFragment extends BaseRegisterFragment {
  private Unbinder unbinder;

  private View.OnClickListener nextButtonOnClickListener;

  @Inject
  RegisterData data;

  @BindView(R.id.button_move_to_next_screen)
  Button nextButton;

  static ProfilePictureFormFragment create() {
    return new ProfilePictureFormFragment();
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

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Attaches the next button to the presenter.
    nextButtonOnClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO
      }
    };
    nextButton.setOnClickListener(nextButtonOnClickListener);
  }

  @Override
  public void onStop() {
    super.onStop();
    // Detaches the next button from the presenter.
    nextButton.setOnClickListener(null);
    nextButtonOnClickListener = null;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Unbinds all annotated resources, views and methods.
    unbinder.unbind();
  }
}
