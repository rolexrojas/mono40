package com.tpago.movil.dep.init.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.tpago.movil.R;
import com.tpago.movil.dep.content.StringResolver;
import com.tpago.movil.dep.text.BaseTextWatcher;
import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.dep.widget.TextInput;
import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author hecvasro
 */
public final class EmailRegisterFormFragment
  extends RegisterFormFragment<EmailRegisterFormPresenter>
  implements EmailRegisterFormPresenter.View {

  static EmailRegisterFormFragment create() {
    return new EmailRegisterFormFragment();
  }

  private EmailRegisterFormPresenter presenter;

  private TextWatcher textInputTextWatcher;
  private TextWatcher confirmationTextInputTextWatcher;

  @Inject StringResolver stringResolver;
  @Inject RegisterData registerData;

  @BindView(R.id.text_input) TextInput textInput;
  @BindView(R.id.text_input_confirmation) TextInput confirmationTextInput;

  @Override
  protected EmailRegisterFormPresenter getPresenter() {
    if (ObjectHelper.isNull(presenter)) {
      presenter = new EmailRegisterFormPresenter(this, stringResolver, registerData);
    }
    return presenter;
  }

  @Override
  protected Fragment getNextScreen() {
    return PasswordRegisterFormFragment.create();
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
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(R.layout.fragment_register_form_email, container, false);
  }

  @Override
  public void onResume() {
    super.onResume();
    // Shows the keyboard.
    Keyboard.show(textInput);
    // Attaches the text input to the presenter.
    textInputTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        getPresenter().onTextInputContentChanged(s.toString());
      }
    };
    textInput.addTextChangedListener(textInputTextWatcher);
    // Attaches the confirmation text input to the presenter.
    confirmationTextInputTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        getPresenter().onConfirmationTextInputContentChanged(s.toString());
      }
    };
    confirmationTextInput.addTextChangedListener(confirmationTextInputTextWatcher);
    confirmationTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          getPresenter().onMoveToNextScreenButtonClicked();
        }
        return false;
      }
    });
  }

  @Override
  public void onPause() {
    super.onPause();
    // Detaches the confirmation text input from the presenter.
    confirmationTextInput.setOnEditorActionListener(null);
    confirmationTextInput.removeTextChangedListener(confirmationTextInputTextWatcher);
    confirmationTextInputTextWatcher = null;
    // Detaches the text input to the presenter.
    textInput.removeTextChangedListener(textInputTextWatcher);
    textInputTextWatcher = null;
  }

  @Override
  public void setTextInputContent(String content) {
    textInput.setText(content);
  }

  @Override
  public void showTextInputContentAsErratic(boolean showAsErratic) {
    textInput.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public void setConfirmationTextInputContent(String content) {
    confirmationTextInput.setText(content);
  }

  @Override
  public void showConfirmationTextInputContentAsErratic(boolean showAsErratic) {
    confirmationTextInput.setErraticStateEnabled(showAsErratic);
  }
}
