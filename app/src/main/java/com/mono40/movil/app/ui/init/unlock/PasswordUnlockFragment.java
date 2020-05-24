package com.mono40.movil.app.ui.init.unlock;

import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mono40.movil.app.ui.FragmentActivityBase;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;
import com.mono40.movil.app.ui.init.unlock.ChangePassword.ChangePasswordFragment;
import com.mono40.movil.lib.Password;
import com.mono40.movil.R;
import com.mono40.movil.dep.init.InitActivityBase;
import com.mono40.movil.dep.text.BaseTextWatcher;
import com.mono40.movil.dep.widget.TextInput;
import com.mono40.movil.session.User;
import com.mono40.movil.util.ChangePasswordRadioMenuUtil;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.RadioGroupUtil;
import com.mono40.movil.util.Result;
import com.mono40.movil.util.StringHelper;
import com.mono40.movil.util.UiUtil;

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
  private Dialog restorePasswordDialog;
  private int selectedOption = 0;

  private void afterUserPasswordTextInputChanged(String s) {
    this.userPassword = s;
    if (!StringHelper.isNullOrEmpty(this.userPassword)) {
      UiUtil.setEnabled(unlockButton, true);
      this.userPasswordTextInput.setErraticStateEnabled(false);
    } else {
      UiUtil.setEnabled(unlockButton, false);
    }
  }

  @Override
  protected void handleSuccess(Result<?> result) {
    if(result.isSuccessful()){
      // Check if user should change password
      Result<User> userResult = (Result<User>) result;
      if(userResult.successData().passwordChanges()){
        this.openChangePassword(false, false);
      }else {
        super.handleSuccess(result);
      }
    }else{
      super.handleSuccess(result);
    }

    if (!result.isSuccessful()) {
      this.userPassword = null;
      this.userPasswordTextInput.setText(null);
    }
  }

  @OnClick(R.id.unlockButton)
  final void onUnlockButtonClicked() {
    if (!StringHelper.isNullOrEmpty(this.userPassword)) {
      this.disposable = this.sessionManager
        .openSession(Password.create(this.userPassword), this.deviceIdSupplier.get())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe((disposable) -> this.takeoverLoader.show())
        .doFinally(this.takeoverLoader::hide)
        .subscribe(this::handleSuccess, this::handleError);
    } else {
      UiUtil.setEnabled(this.unlockButton, false);
      this.userPasswordTextInput.setErraticStateEnabled(true);

      this.alertManager.builder()
        .title(R.string.register_form_password_error_title)
        .message(R.string.register_form_password_error_message)
        .show();
    }
  }

  private void openChangePassword(boolean shouldRequestPIN, boolean shouldCloseSession) {
    this.startActivity(
    	FragmentActivityBase.createLaunchIntent(
				this.getContext(),
				ChangePasswordFragment.creator()
      ).putExtra(ChangePasswordFragment.SHOULD_REQUEST_PIN,shouldRequestPIN)
       .putExtra(ChangePasswordFragment.SHOULD_CLOSE_SESSION, shouldCloseSession)
    );
  }

  @OnClick(R.id.userForgotPassword)
  final void onUserForgotPasswordViewClicked() {
    if(ObjectHelper.isNull(restorePasswordDialog)){
      restorePasswordDialog = createDialog();
    }
    restorePasswordDialog.show();
  }

  private void moveTo(Fragment screen) {
    this.fragmentReplacer.begin(screen)
      .transition(FragmentReplacer.Transition.FIFO)
      .addToBackStack(true, "Email")
      .commit();
  }

  private Dialog createDialog() {
    String[] groupName = { getString(R.string.reset_with_pin), getString(R.string.reset_with_email) };
    Dialog dialog = ChangePasswordRadioMenuUtil.createChangePasswordRadioMenuDialog(getActivity());
    RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);

    TextView cancel = dialog.findViewById(R.id.cancel_action);
    TextView confirm = dialog.findViewById(R.id.do_action);
    UiUtil.setEnabled(confirm, false);

    cancel.setOnClickListener((view) -> dialog.cancel());

    confirm.setOnClickListener((view -> {
      radioGroup.setOnCheckedChangeListener(null);
      radioGroup.clearCheck();
      radioGroup.removeAllViews();
      dialog.cancel();

      switch (selectedOption) {
        case 0:
          openChangePassword(true, false);
          break;
        case 1:
          moveTo(EmailPasswordFragment.create());
          break;
      }
    }));

    RadioGroupUtil.setRadioButtons(radioGroup, groupName, getActivity());

    radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
      selectedOption = Integer.valueOf(group.getCheckedRadioButtonId());
      UiUtil.setEnabled(confirm, true);
    });

    return dialog;
  }

  @Override
  protected int layoutResId() {
    return R.layout.unlock_password;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    InitActivityBase.get(this.getActivity())
      .getInitComponent()
      .inject(this);
    UiUtil.setEnabled(this.unlockButton, false);
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
    this.userPasswordTextWatcher = null;
    this.restorePasswordDialog = null;
    super.onPause();
  }
}
