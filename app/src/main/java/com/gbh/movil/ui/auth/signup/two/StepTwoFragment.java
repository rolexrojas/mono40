package com.gbh.movil.ui.auth.signup.two;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gbh.movil.R;
import com.gbh.movil.misc.Utils;
import com.gbh.movil.ui.ChildFragment;
import com.gbh.movil.ui.auth.signup.SignUpContainer;
import com.gbh.movil.ui.auth.signup.three.StepThreeFragment;
import com.gbh.movil.ui.text.UiTextHelper;
import com.jakewharton.rxbinding.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public class StepTwoFragment extends ChildFragment<SignUpContainer> implements StepTwoScreen {
  /**
   * TODO
   */
  private static final String ARG_PHONE_NUMBER = "phoneNumber";
  /**
   * TODO
   */
  private static final String ARG_EMAIL = "email";

  private Unbinder unbinder;

  @Inject
  StepTwoPresenter presenter;

  @BindView(R.id.edit_text_password)
  EditText passwordEditText;
  @BindView(R.id.edit_text_password_confirmation)
  EditText confirmationEditText;
  @BindView(R.id.button_continue)
  Button submitButton;

  /**
   * TODO
   *
   * @param phoneNumber
   *   TODO
   * @param email
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static StepTwoFragment newInstance(@NonNull String phoneNumber, @NonNull String email) {
    final Bundle bundle = new Bundle();
    bundle.putString(ARG_PHONE_NUMBER, phoneNumber);
    bundle.putString(ARG_EMAIL, email);
    final StepTwoFragment fragment = new StepTwoFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final Bundle bundle = Utils.isNotNull(savedInstanceState) ? savedInstanceState : getArguments();
    if (Utils.isNull(bundle)) {
      throw new NullPointerException(String.format("Arguments '%1$s' and '%2$s' are missing",
        ARG_PHONE_NUMBER, ARG_EMAIL));
    } else if (!bundle.containsKey(ARG_PHONE_NUMBER)) {
      throw new NullPointerException("Argument '" + ARG_PHONE_NUMBER + "' is missing");
    } else if (!bundle.containsKey(ARG_EMAIL)) {
      throw new NullPointerException("Argument '" + ARG_EMAIL + "' is missing");
    } else {
      final StepTwoComponent component = DaggerStepTwoComponent.builder()
        .signUpComponent(getContainer().getComponent())
        .stepTwoModule(new StepTwoModule(bundle.getString(ARG_PHONE_NUMBER),
          bundle.getString(ARG_EMAIL)))
        .build();
      component.inject(this);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.screen_sign_up_step_two, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Starts the presenter.
    presenter.start();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @NonNull
  @Override
  public Observable<String> passwordChanges() {
    return UiTextHelper.textChanges(passwordEditText);
  }

  @NonNull
  @Override
  public Observable<String> passwordConfirmationChanges() {
    return UiTextHelper.textChanges(confirmationEditText);
  }

  @NonNull
  @Override
  public Observable<Void> submitButtonClicks() {
    return RxView.clicks(submitButton);
  }

  @Override
  public void setPasswordError(@Nullable String message) {
    // TODO
  }

  @Override
  public void setPasswordConfirmationError(@Nullable String message) {
    // TODO
  }

  @Override
  public void setSubmitButtonEnabled(boolean enabled) {
    submitButton.setEnabled(enabled);
  }

  @Override
  public void submit() {
    getContainer().setChildFragment(StepThreeFragment.newInstance(), true, true);
  }
}
