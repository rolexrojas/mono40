package com.mono40.movil.dep.init.capture;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mono40.movil.R;
import com.mono40.movil.dep.content.StringResolver;
import com.mono40.movil.dep.text.BaseTextWatcher;
import com.mono40.movil.dep.widget.Keyboard;
import com.mono40.movil.dep.widget.TextInput;
import com.mono40.movil.util.ObjectHelper;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author hecvasro
 */
public final class NameCaptureFormFragment
  extends CaptureFormFragment<NameCaptureFormPresenter>
  implements NameCaptureFormPresenter.View {

  static NameCaptureFormFragment create() {
    return new NameCaptureFormFragment();
  }

  private NameCaptureFormPresenter presenter;
  private TextWatcher firstNameTextWatcher;
  // Attaches the last updateName text input to the presenter.
  private TextWatcher lastNameTextWatcher = new BaseTextWatcher() {
    @Override
    public void afterTextChanged(Editable s) {
      getPresenter().onLastNameTextInputContentChanged(s.toString());
    }
  };

  @BindView(R.id.text_input_first_name) TextInput firstNameTextInput;
  @BindView(R.id.text_input_last_name) TextInput lastNameTextInput;

  @Inject StringResolver stringResolver;
  @Inject CaptureData captureData;

  @Override
  protected NameCaptureFormPresenter getPresenter() {
    if (ObjectHelper.isNull(presenter)) {
      presenter = new NameCaptureFormPresenter(this, stringResolver, captureData);
    }
    return presenter;
  }

  @Override
  protected Fragment getNextScreen() {
    return EmailCaptureFormFragment.create();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    getCaptureComponent()
      .inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
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
    lastNameTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        getPresenter().onLastNameTextInputContentChanged(s.toString());
      }
    };
    lastNameTextInput.addTextChangedListener(lastNameTextWatcher);
    lastNameTextInput.setOnEditorActionListener((view, actionId, event) -> {
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        getPresenter().onMoveToNextScreenButtonClicked();
      }
      return false;
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
