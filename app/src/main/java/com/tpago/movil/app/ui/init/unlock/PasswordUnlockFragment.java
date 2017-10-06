package com.tpago.movil.app.ui.init.unlock;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import com.tpago.movil.Password;
import com.tpago.movil.R;
import com.tpago.movil.app.ui.AlertData;
import com.tpago.movil.dep.init.InitActivity;
import com.tpago.movil.dep.text.BaseTextWatcher;
import com.tpago.movil.dep.widget.TextInput;
import com.tpago.movil.util.Result;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hecvasro
 */
public final class PasswordUnlockFragment extends BaseUnlockFragment {

  static PasswordUnlockFragment create() {
    return new PasswordUnlockFragment();
  }

  @BindView(R.id.unlockButton) Button unlockButton;

  @BindView(R.id.userPasswordTextInput) TextInput userPasswordTextInput;
  private String userPassword;
  private TextWatcher userPasswordTextWatcher;

  private void afterUserPasswordTextInputChanged(String s) {
    this.userPassword = s;
    if (Password.isValid(this.userPassword)) {
      this.unlockButton.setAlpha(1.00F);
      this.userPasswordTextInput.setErraticStateEnabled(false);
    }
  }

  @Override
  protected void handleSuccess(Result<?> result) {
    super.handleSuccess(result);

    if (!result.isSuccessful()) {
      this.userPassword = null;
      this.userPasswordTextInput.setText(null);
    }
  }

  @OnClick(R.id.unlockButton)
  final void onUnlockButtonClicked() {
    if (Password.isValid(this.userPassword)) {
      this.disposable = this.sessionManager
        .openSession(Password.create(this.userPassword), this.deviceIdSupplier.get())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((disposable) -> this.takeoverLoader.show())
        .doFinally(this.takeoverLoader::hide)
        .subscribe(this::handleSuccess, this::handleError);
    } else {
      this.unlockButton.setAlpha(0.50F);
      this.userPasswordTextInput.setErraticStateEnabled(true);

      final AlertData alertData = AlertData.builder(this.stringMapper)
        .title(R.string.register_form_password_error_title)
        .message(R.string.register_form_password_error_message)
        .build();
      this.alertManager.show(alertData);
    }
  }

  @Override
  protected int layoutResId() {
    return R.layout.unlock_password;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    InitActivity.get(this.getActivity())
      .getInitComponent()
      .inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    this.logoAnimator.moveOutOfScreen();

    this.userPasswordTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        afterUserPasswordTextInputChanged(s.toString());
      }
    };
    this.userPasswordTextInput.addTextChangedListener(this.userPasswordTextWatcher);
    this.userPasswordTextInput.setOnEditorActionListener((v, id, e) -> {
      if (id == EditorInfo.IME_ACTION_DONE) {
        this.onUnlockButtonClicked();
      }
      return false;
    });
  }

  @Override
  public void onPause() {
    this.userPasswordTextInput.setOnEditorActionListener(null);
    this.userPasswordTextInput.removeTextChangedListener(this.userPasswordTextWatcher);
    this.userPasswordTextInput = null;

    super.onPause();
  }
}
