package com.tpago.movil.app.ui.main.settings.profile.change.password;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.Input;
import com.tpago.movil.app.ui.activity.toolbar.ActivityToolbar;
import com.tpago.movil.app.ui.fragment.base.FragmentBase;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.function.Consumer;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author hecvasro
 */
public final class FragmentChangePassword extends FragmentBase
  implements PresentationChangePassword {

  public static FragmentChangePassword create() {
    return new FragmentChangePassword();
  }

  @BindView(R.id.input_password) Input passwordInput;
  @BindView(R.id.input_value) Input valueInput;
  @BindView(R.id.input_value_confirmation) Input valueConfirmationInput;

  @Inject PresenterChangePassword presenter;

  private MenuItem submitButton;

  private TextWatcher passwordWatcher;
  private TextWatcher valueWatcher;
  private TextWatcher valueConfirmationWatcher;

  @LayoutRes
  @Override
  protected int layoutResId() {
    return R.layout.settings_profile_change_password;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the dependency injector.
    final FragmentComponentChangePassword component = ActivityToolbar.get(this.getContext())
      .retainedFragment()
      .componentBuilderSupplier()
      .get(
        FragmentChangePassword.class,
        FragmentComponentChangePassword.Builder.class
      )
      .changePassword(FragmentModuleChangePassword.create(this))
      .build();

    // Injects all annotated dependencies.
    component.inject(this);

    // Sets the options menu.
    this.setHasOptionsMenu(true);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    // Binds the submit button from the fragment.
    inflater.inflate(R.menu.settings_profile_change_password, menu);

    this.submitButton = menu.findItem(R.id.menu_item_submit);
  }

  private static TextWatcher createWatcher(final Consumer<String> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        consumer.accept(s.toString());
      }
    };
  }

  @Override
  public void onResume() {
    super.onResume();

    // Resumes the presenter.
    this.presenter.onPresentationResumed();

    // Binds all the inputs to the presenter.
    this.passwordWatcher = createWatcher(this.presenter::onPasswordChanged);
    this.passwordInput.addTextChangedListener(this.passwordWatcher);
    this.valueWatcher = createWatcher(this.presenter::onValueChanged);
    this.valueInput.addTextChangedListener(this.valueWatcher);
    this.valueConfirmationWatcher = createWatcher(this.presenter::onValueConfirmationChanged);
    this.valueConfirmationInput.addTextChangedListener(this.valueConfirmationWatcher);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.menu_item_submit) {
      this.presenter.onSubmitButtonPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onPause() {
    // Unbinds all the inputs from the presenter.
    this.valueConfirmationInput.removeTextChangedListener(this.valueConfirmationWatcher);
    this.valueConfirmationWatcher = null;
    this.valueInput.removeTextChangedListener(this.valueWatcher);
    this.valueWatcher = null;
    this.passwordInput.removeTextChangedListener(this.passwordWatcher);
    this.passwordWatcher = null;

    // Pauses the presenter.
    this.presenter.onPresentationPaused();

    super.onPause();
  }

  @Override
  public void onDestroyOptionsMenu() {
    // Unbinds the submit button from the fragment.
    this.submitButton = null;

    super.onDestroyOptionsMenu();
  }

  @Override
  public void showPasswordInputAsErratic(boolean showAsErratic) {
    this.passwordInput.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public void showValueInputAsErratic(boolean showAsErratic) {
    this.valueInput.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public void showValueConfirmationAsErratic(boolean showAsErratic) {
    this.valueConfirmationInput.setErraticStateEnabled(showAsErratic);
  }

  @Override
  public void showSubmitButtonAsEnabled(boolean showAsEnabled) {
    this.submitButton.setEnabled(showAsEnabled);
  }
}
