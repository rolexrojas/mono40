package com.tpago.movil.dep.init.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.ActivityQualifier;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.dep.init.BaseInitFragment;
import com.tpago.movil.dep.init.InitFragment;
import com.tpago.movil.dep.init.LogoAnimator;
import com.tpago.movil.dep.text.BaseTextWatcher;
import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.dep.widget.TextInput;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class SignInFragment extends BaseInitFragment implements SignInPresenter.View {

  public static SignInFragment create() {
    return new SignInFragment();
  }

  private Unbinder unbinder;

  private TextWatcher emailTextInputTextWatcher;
  private TextWatcher passwordTextInputTextWatcher;

  private SignInPresenter presenter;

  @Inject
  @ActivityQualifier
  FragmentReplacer fragmentReplacer;

  @Inject LogoAnimator logoAnimator;

  @BindView(R.id.text_input_email) TextInput emailTextInput;
  @BindView(R.id.text_input_password) TextInput passwordTextInput;
  @BindView(R.id.button_sign_in) Button signInButton;

  @OnClick(R.id.button_sign_in)
  void onSignInButtonClicked() {
    presenter.onSignInButtonClicked();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all the annotated dependencies.
    getInitComponent()
      .inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.fragment_sign_in, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates the presenter.
    presenter = new SignInPresenter(this, getInitComponent());
  }

  @Override
  public void onStart() {
    super.onStart();
    // Notifies the presenter that the view its being started.
    presenter.onViewStarted();
  }

  @Override
  public void onResume() {
    super.onResume();
    // Moves the logo out of the screen.
    logoAnimator.moveOutOfScreen();
    // Shows the keyboard.
    Keyboard.show(emailTextInput);
    // Attaches the email text input to the presenter.
    emailTextInputTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        presenter.onEmailTextInputContentChanged(s.toString());
      }
    };
    emailTextInput.addTextChangedListener(emailTextInputTextWatcher);
    // Attaches the password text input to the presenter.
    passwordTextInputTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        presenter.onPasswordTextInputContentChanged(s.toString());
      }
    };
    passwordTextInput.addTextChangedListener(passwordTextInputTextWatcher);
    passwordTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          presenter.onSignInButtonClicked();
        }
        return false;
      }
    });
  }

  @Override
  public void onPause() {
    super.onPause();
    // Detaches the last updateName text input from the presenter.
    passwordTextInput.setOnEditorActionListener(null);
    passwordTextInput.removeTextChangedListener(passwordTextInputTextWatcher);
    passwordTextInputTextWatcher = null;
    // Detaches the first updateName text input from the presenter.
    emailTextInput.removeTextChangedListener(emailTextInputTextWatcher);
    emailTextInputTextWatcher = null;
  }

  @Override
  public void onStop() {
    super.onStop();
    // Notifies the presenter that the view its being stopped.
    presenter.onViewStopped();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the presenter.
    presenter = null;
    // Unbinds all the annotated resources, views and method.
    unbinder.unbind();
  }

  @Override
  public void setEmailTextInputContent(String content) {
    emailTextInput.setText(content);
  }

  @Override
  public void showEmailTextInputAsErratic(boolean showAsErratic) {
    emailTextInput.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public void setPasswordTextInputContent(String content) {
    passwordTextInput.setText(content);
  }

  @Override
  public void showPasswordTextInputAsErratic(boolean showAsErratic) {
    passwordTextInput.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public void setSignInButtonEnabled(boolean enabled) {
    signInButton.setEnabled(enabled);
  }

  @Override
  public void showSignInButtonAsEnabled(boolean showAsEnabled) {
    signInButton.setAlpha(showAsEnabled ? 1.0F : 0.5F);
  }

  @Override
  public void moveToInitScreen() {
    fragmentReplacer.begin(InitFragment.create())
      .transition(FragmentReplacer.Transition.FIFO)
      .commit();
  }
}
