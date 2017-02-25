package com.tpago.movil.onboarding.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.tpago.movil.app.InformationalDialogFragment;
import com.tpago.movil.content.StringResolver;
import com.tpago.movil.text.BaseTextWatcher;
import com.tpago.movil.widget.TextInput;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class NameFormFragment
  extends ChildRegistrationFragment
  implements NameFormPresenter.View {
  private Unbinder unbinder;
  private NameFormPresenter presenter;

  private TextWatcher firstNameTextWatcher;
  private TextWatcher lastNameTextWatcher;
  private TextView.OnEditorActionListener lastNameOnEditorActionListener;

  private View.OnClickListener nextButtonOnClickListener;

  @BindView(R.id.text_input_first_name)
  TextInput firstNameTextInput;
  @BindView(R.id.text_input_last_name)
  TextInput lastNameTextInput;
  @BindView(R.id.button_next)
  Button nextButton;

  @Inject
  RegistrationData data;
  @Inject
  StringResolver stringResolver;

  static NameFormFragment create() {
    return new NameFormFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    getRegistrationComponent().inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_name_form, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates the presenter.
    presenter = new NameFormPresenter(data, stringResolver);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Attaches itself to the presenter.
    presenter.setView(this);
    // Attaches the first name text input to the presenter.
    firstNameTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        presenter.onFirstNameTextInputContentChanged(s.toString());
      }
    };
    firstNameTextInput.addTextChangedListener(firstNameTextWatcher);
    // Attaches the last name text input to the presenter.
    lastNameTextWatcher = new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        presenter.onLastNameTextInputContentChanged(s.toString());
      }
    };
    lastNameTextInput.addTextChangedListener(lastNameTextWatcher);
    lastNameOnEditorActionListener = new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          presenter.onNextButtonClicked();
        }
        return false;
      }
    };
    lastNameTextInput.setOnEditorActionListener(lastNameOnEditorActionListener);
    // Attaches the next button to the presenter.
    nextButtonOnClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        presenter.onNextButtonClicked();
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
    // Attaches the last name text input from the presenter.
    lastNameTextInput.setOnEditorActionListener(null);
    lastNameOnEditorActionListener = null;
    lastNameTextInput.removeTextChangedListener(lastNameTextWatcher);
    lastNameTextWatcher = null;
    // Attaches the first name text input from the presenter.
    firstNameTextInput.removeTextChangedListener(firstNameTextWatcher);
    lastNameTextWatcher = null;
    // Detaches itself from the presenter.
    presenter.setView(null);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Destroys the presenter.
    presenter = null;
    // Unbinds all annotated resources, views and methods.
    unbinder.unbind();
  }

  @Override
  public void showDialog(String title, String message, String positiveButtonText) {
    final DialogFragment fragment = InformationalDialogFragment.create(
      title,
      message,
      positiveButtonText);
    fragment.setTargetFragment(this, 0);
    fragment.show(getChildFragmentManager(), null);
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

  @Override
  public void showNextButtonAsEnabled(boolean showAsEnabled) {
    nextButton.setAlpha(showAsEnabled ? 1.0F : 0.5F);
  }

  @Override
  public void moveToNextScreen() {
    // TODO
  }
}
