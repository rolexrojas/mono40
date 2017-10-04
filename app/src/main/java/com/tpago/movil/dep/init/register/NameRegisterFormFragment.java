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
import com.tpago.movil.dep.Objects;
import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.dep.widget.TextInput;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author hecvasro
 */
public final class NameRegisterFormFragment
  extends RegisterFormFragment<NameRegisterFormPresenter>
  implements NameRegisterFormPresenter.View {
  static NameRegisterFormFragment create() {
    return new NameRegisterFormFragment();
  }

  private NameRegisterFormPresenter presenter;

  private TextWatcher firstNameTextWatcher;
  private TextWatcher lastNameTextWatcher;

  @BindView(R.id.text_input_first_name) TextInput firstNameTextInput;
  @BindView(R.id.text_input_last_name) TextInput lastNameTextInput;

  @Inject StringResolver stringResolver;
  @Inject RegisterData registerData;

  @Override
  protected NameRegisterFormPresenter getPresenter() {
    if (Objects.checkIfNull(presenter)) {
      presenter = new NameRegisterFormPresenter(this, stringResolver, registerData);
    }
    return presenter;
  }

  @Override
  protected Fragment getNextScreen() {
    return AvatarFormFragment.create();
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
    return inflater.inflate(R.layout.fragment_register_form_name, container, false);
  }

  @Override
  public void onResume() {
    super.onResume();
    // Shows the keyboard.
    Keyboard.show(firstNameTextInput);
    // Attaches the first updateName text input to the presenter.
    firstNameTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        getPresenter().onFirstNameTextInputContentChanged(s.toString());
      }
    };
    firstNameTextInput.addTextChangedListener(firstNameTextWatcher);
    // Attaches the last updateName text input to the presenter.
    lastNameTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        getPresenter().onLastNameTextInputContentChanged(s.toString());
      }
    };
    lastNameTextInput.addTextChangedListener(lastNameTextWatcher);
    lastNameTextInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
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
    // Detaches the last updateName text input from the presenter.
    lastNameTextInput.setOnEditorActionListener(null);
    lastNameTextInput.removeTextChangedListener(lastNameTextWatcher);
    lastNameTextWatcher = null;
    // Detaches the first updateName text input from the presenter.
    firstNameTextInput.removeTextChangedListener(firstNameTextWatcher);
    lastNameTextWatcher = null;
  }

  @Override
  public void setFirstNameTextInputContent(String content) {
    firstNameTextInput.setText(content);
  }

  @Override
  public void showFirstNameTextInputAsErratic(boolean showAsErratic) {
    firstNameTextInput.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public void setLastNameTextInputContent(String content) {
    lastNameTextInput.setText(content);
  }

  @Override
  public void showLastNameTextInputAsErratic(boolean showAsErratic) {
    lastNameTextInput.setErraticStateEnabled(showAsErratic);
  }
}
