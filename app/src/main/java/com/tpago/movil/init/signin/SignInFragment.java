package com.tpago.movil.init.signin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tpago.movil.R;
import com.tpago.movil.app.ActivityQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.app.InformationalDialogFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.init.BaseInitFragment;
import com.tpago.movil.init.InitFragment;
import com.tpago.movil.init.LogoAnimator;
import com.tpago.movil.text.BaseTextWatcher;
import com.tpago.movil.widget.FullSizeLoadIndicator;
import com.tpago.movil.widget.Keyboard;
import com.tpago.movil.widget.LoadIndicator;
import com.tpago.movil.widget.TextInput;

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
  private LoadIndicator loadIndicator;

  private TextWatcher emailTextInputTextWatcher;
  private TextWatcher passwordTextInputTextWatcher;

  private SignInPresenter presenter;

  @Inject LogoAnimator logoAnimator;
  @Inject @ActivityQualifier FragmentReplacer fragmentReplacer;

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
    getInitComponent().inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_sign_in, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates the load indicator.
    loadIndicator = new FullSizeLoadIndicator(getChildFragmentManager());
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
    // Detaches the last name text input from the presenter.
    passwordTextInput.setOnEditorActionListener(null);
    passwordTextInput.removeTextChangedListener(passwordTextInputTextWatcher);
    passwordTextInputTextWatcher = null;
    // Detaches the first name text input from the presenter.
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
    // Destroys the load indicator.
    loadIndicator = null;
    // Unbinds all the annotated resources, views and method.
    unbinder.unbind();
  }

  @Override
  public void showDialog(int titleId, String message, int positiveButtonTextId) {
    InformationalDialogFragment.create(getString(titleId), message, getString(positiveButtonTextId))
      .show(getChildFragmentManager(), null);
  }

  @Override
  public void showDialog(int titleId, int messageId, int positiveButtonTextId) {
    showDialog(titleId, getString(messageId), positiveButtonTextId);
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
  public void startLoading() {
    loadIndicator.start();
  }

  @Override
  public void stopLoading() {
    loadIndicator.stop();
  }

  @Override
  public void checkIfUserWantsToForceSignIn() {
    new AlertDialog.Builder(getActivity())
      .setTitle(R.string.dialog_title_already_associated_device)
      .setMessage(R.string.dialog_message_already_associated_device)
      .setNegativeButton(R.string.dialog_negative_text_already_associated_device, null)
      .setPositiveButton(
        R.string.dialog_positive_text_already_associated_device,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            presenter.onSignInForcingButtonClicked();
          }
        })
      .create()
      .show();
  }

  @Override
  public void moveToInitScreen() {
    fragmentReplacer.begin(InitFragment.create())
      .setTransition(FragmentReplacer.Transition.FIFO)
      .commit();
  }

  @Override
  public void showGenericErrorDialog(String message) {
    Dialogs.builder(getContext())
      .setTitle(R.string.error_generic_title)
      .setMessage(message)
      .setPositiveButton(R.string.error_positive_button_text, null)
      .show();
  }

  @Override
  public void showGenericErrorDialog() {
    showGenericErrorDialog(getString(R.string.error_generic));
  }

  @Override
  public void showUnavailableNetworkError() {
    Toast.makeText(getContext(), R.string.error_unavailable_network, Toast.LENGTH_LONG).show();
  }
}
