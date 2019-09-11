package com.tpago.movil.app.ui.init.unlock.ChangePassword;

import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.google.auto.value.AutoValue;
import com.tpago.movil.R;
import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.FragmentActivityBase;
import com.tpago.movil.app.ui.FragmentCreator;
import com.tpago.movil.app.ui.fragment.base.FragmentBase;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.dep.App;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.dep.text.BaseTextWatcher;
import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.dep.widget.TextInput;
import com.tpago.movil.lib.Password;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordFragment extends FragmentBase implements ChangePasswordPresentation {


  public static String SHOULD_REQUEST_PIN = "shouldRequestPIN";
  public static String SHOULD_CLOSE_SESSION = "shouldCloseSession";
  public static String SHOULD_DESTROY_SESSION = "shouldDestroySession";


  private TextWatcher newPasswordTextWatcher;
  private TextWatcher confirmPasswordTextWatcher;
  private boolean shouldRequestPIN;
  private boolean shouldCloseSession;
  private boolean shouldDestroySession;

  @BindView(R.id.new_password_input)
  TextInput newPasswordTextInput;
  @BindView(R.id.confirm_password_input)
  TextInput confirmPasswordTextInput;
  FragmentActivityBase activity;

  @Inject Api api;
  @Inject NetworkService networkService;
  @Inject SessionManager sessionManager;
  public ChangePasswordFragment() {
    // Required empty public constructor
  }

  public static FragmentCreator creator() {
    return new AutoValue_ChangePasswordFragment_Creator();
  }

  private void onNewPasswordInputChanged(String s) {
    if (StringHelper.isNullOrEmpty(s)) {
      showNewPasswordTextInputContentAsErratic(false);
      showPasswordConfirmationTextInputContentAsErratic(false);
      this.activity.setChangePasswordOkButtonEnabled(false);
    }else{
      if(isConfirmationPasswordInputContent()){
        this.activity.setChangePasswordOkButtonEnabled(true);
      }
    }
  }

  private void onConfirmInputChanged(String s) {
    if (isConfirmationPasswordInputContent()) {
      showPasswordConfirmationTextInputContentAsErratic(false);
      this.activity.setChangePasswordOkButtonEnabled(true);
    }else{
      showPasswordConfirmationTextInputContentAsErratic(true);
      this.activity.setChangePasswordOkButtonEnabled(false);
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Injects all the annotated dependencies.
    App.get(this.getContext())
            .component()
            .inject(this);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    this.activity = FragmentActivityBase.get(this.getActivity());
    this.activity.showChangePasswordLayout();
    this.activity.setChangePasswordOkButtonEnabled(false);
    this.shouldRequestPIN = this.activity.getIntent().getExtras().getBoolean(SHOULD_REQUEST_PIN);
    this.shouldCloseSession = this.activity.getIntent().getExtras().getBoolean(SHOULD_CLOSE_SESSION);
    this.shouldDestroySession = this.activity.getIntent().getExtras().getBoolean(SHOULD_DESTROY_SESSION);
    this.setListeners();
  }


  // TODO: REFACTOR
  private void setListeners() {

    this.activity.setOnChangePasswordOkButtonClickListener(v -> {
      if(isNewPasswordInputContent() && isConfirmationPasswordInputContent()){
        final String newPassword = newPasswordTextInput.getText().toString();
        if(this.shouldRequestPIN){
          //Request for the correct PIN, Make API Call for restoring password & Move to Next Screen

          final Display display = getActivity().getWindowManager().getDefaultDisplay();
          final Point size = new Point();
          display.getSize(size);

          final int x = size.x / 2;
          final int y = size.y / 2;

          PinConfirmationDialogFragment.show(
            getChildFragmentManager(),
            getString(R.string.recoverpasswordhint),
              new PinConfirmationDialogFragment.Callback() {
                @Override
                public void confirm(String pin) {
                  final String msisdn = sessionManager.getUser().phoneNumber().value();
                  final String email = sessionManager.getUser().email().value();

                  if(networkService.checkIfAvailable()){
                    api.ResetPasswordWithPIN(newPassword, msisdn, email, pin)
                      .subscribeOn(Schedulers.io())
                      .unsubscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe((result) -> {
                        if(result.isSuccessful()){
                          handleSuccess();
                        }else{
                          if(ObjectHelper.isNull(result.failureData())){
                            onPINConfirmationHandleError(getString(R.string.error_generic));
                          }else{
                            onPINConfirmationHandleError(result.failureData().description());
                          }
                        }
                      });
                  }else{
                    showUnavailableNetworkError();
                  }
                }
              },
              x,
              y
          );
        }else {
          if(!this.sessionManager.isSessionOpen()){
            this.activity.finish();
          }else{
            if(networkService.checkIfAvailable()){
              api.ChangePassword(newPassword)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                  if(result.isSuccessful()){
                    handleSuccess();
                  }else{
                    if(ObjectHelper.isNull(result.failureData())){
                      handleError(getString(R.string.error_generic));
                    }else{
                      handleError(result.failureData().description());
                    }
                  }
                });
              this.closeSession();
            }else{
              showUnavailableNetworkError();
            }
          }
        }
      } else {

        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.register_form_password_error_title));
        builder.setMessage(getString(R.string.register_form_password_error_message));
        builder.setPositiveButton(getString(R.string.register_form_password_error_positive_button_text),
                (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        showPasswordConfirmationTextInputContentAsErratic(true);
        showNewPasswordTextInputContentAsErratic(true);
      }
    });

    this.activity.setOnChangePasswordCancelButtonClickListener(v -> {
      this.closeSession();
      this.finish();
    });
  }

  private void handleSuccess() {
    if(shouldDestroySession) {
      this.sessionManager.destroySessionOnSigninForgotPassword();
    }
    Dialogs.builder(getContext())
      .setTitle(R.string.done_with_exclamation_mark)
      .setMessage(R.string.succededpasswordtransaction)
      .setPositiveButton(R.string.ok, (dialog, which) -> finish())
      .create()
      .show();
  }

  private void handleError(String errorDescription) {
    Dialogs.builder(getContext())
      .setTitle(R.string.error_generic_title)
      .setMessage(errorDescription)
      .setPositiveButton(R.string.error_positive_button_text, (dialog, which) -> dialog.dismiss())
      .create()
      .show();
  }
  private void onPINConfirmationHandleError (String errorDescription) {
    Dialogs.builder(getContext())
      .setTitle(R.string.error_generic_title)
      .setMessage(errorDescription)
      .setPositiveButton(R.string.error_positive_button_text, (dialog, which) ->
        PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), true))
      .create()
      .show();
  }

  private void closeSession() {
    if (this.sessionManager.isSessionOpen() && this.shouldCloseSession) {
      this.sessionManager.closeSession()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(()->{}, (error) -> handleError(getString(R.string.error_generic)));
    }
  }
  private void showUnavailableNetworkError(){
    Toast.makeText(getContext(), R.string.error_unavailable_network, Toast.LENGTH_LONG)
            .show();
  }
  @Override
  public void onResume() {
    super.onResume();
    this.activity.showChangePasswordLayout();
    Keyboard.show(newPasswordTextInput);

    // Attaches the text input to the presenter.
    newPasswordTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        onNewPasswordInputChanged(s.toString());
      }
    };

    newPasswordTextInput.addTextChangedListener(newPasswordTextWatcher);

    // Attaches the confirmation text input to the presenter.
    confirmPasswordTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        onConfirmInputChanged(s.toString());
      }
    };
    confirmPasswordTextInput.addTextChangedListener(confirmPasswordTextWatcher);
  }

  @Override
  protected int layoutResId() {
    return R.layout.fragment_change_password;
  }

  @Override
  public void showNewPasswordTextInputContentAsErratic(boolean showAsErratic) {
    newPasswordTextInput.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public void showPasswordConfirmationTextInputContentAsErratic(boolean showAsErratic) {
    confirmPasswordTextInput.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public boolean isNewPasswordInputContent() {
    return Password.isValid(newPasswordTextInput.getText().toString())
            && !StringHelper.isNullOrEmpty(newPasswordTextInput.getText().toString());
  }

  @Override
  public boolean isConfirmationPasswordInputContent() {
    return confirmPasswordTextInput.getText().toString().equals(newPasswordTextInput.getText().toString());
  }

  @Override
  public void finish() {
    this.activity.hideChangePasswordLinearLayout();
    getActivity().finish();
  }

  @Override
  public void onPause() {
    this.activity.hideChangePasswordLinearLayout();
    super.onPause();
  }


  @AutoValue
  public static abstract class Creator extends FragmentCreator {

    Creator() {
    }

    @Override
    public Fragment create() {
      return new ChangePasswordFragment();
    }
  }
}
