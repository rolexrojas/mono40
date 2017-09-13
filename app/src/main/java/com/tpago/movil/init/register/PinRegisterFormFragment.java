package com.tpago.movil.init.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tpago.movil.Digit;
import com.tpago.movil.R;
import com.tpago.movil.app.FragmentQualifier;
import com.tpago.movil.app.FragmentReplacer;
import com.tpago.movil.app.InformationalDialogFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.widget.EditableLabel;
import com.tpago.movil.widget.FullSizeLoadIndicator;
import com.tpago.movil.widget.LoadIndicator;
import com.tpago.movil.widget.NumPad;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hecvasro
 */
public final class PinRegisterFormFragment
  extends BaseRegisterFragment
  implements PinRegisterFormPresenter.View,
  NumPad.OnDigitClickedListener,
  NumPad.OnDeleteClickedListener {
  private Unbinder unbinder;
  private LoadIndicator loadIndicator;
  private PinRegisterFormPresenter presenter;

  @Inject @FragmentQualifier FragmentReplacer fragmentReplacer;

  @BindView(R.id.editable_label_pin) EditableLabel pinEditableLabel;
  @BindView(R.id.num_pad) NumPad numPad;

  static PinRegisterFormFragment create() {
    return new PinRegisterFormFragment();
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
    return inflater.inflate(R.layout.fragment_register_form_pin, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated resources, views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Creates the load indicator.
    loadIndicator = new FullSizeLoadIndicator(getChildFragmentManager());
    // Creates the presenter.
    presenter = new PinRegisterFormPresenter(this, getRegisterComponent());
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
    // Adds a listener that gets notified each time a digit button of the num pad is clicked.
    numPad.setOnDigitClickedListener(this);
    // Adds a listener that gets notified each time the delete button of the num pad is clicked.
    numPad.setOnDeleteClickedListener(this);
  }

  @Override
  public void onPause() {
    super.onPause();
    // Removes the listener that gets notified each time a digit button of the num pad is clicked.
    numPad.setOnDigitClickedListener(null);
    // Removes the listener that gets notified each time the delete button of the num pad is clicked.
    numPad.setOnDeleteClickedListener(null);
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
    // Unbinds all the annotated resources, views and methods.
    unbinder.unbind();
  }

  @Override
  public void onDigitClicked(@Digit int digit) {
    presenter.onDigitButtonClicked(digit);
  }

  @Override
  public void onDeleteClicked() {
    presenter.onDeleteButtonClicked();
  }

  @Override
  public void showDialog(int titleId, String message, int positiveButtonTextId) {
    InformationalDialogFragment.create(
      getString(titleId),
      message,
      getString(positiveButtonTextId))
      .show(getChildFragmentManager(), null);
  }

  @Override
  public void showDialog(int titleId, int messageId, int positiveButtonTextId) {
    showDialog(titleId, getString(messageId), positiveButtonTextId);
  }

  @Override
  public void setTextInputContent(String content) {
    pinEditableLabel.setText(content);
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
  public void moveToNextScreen() {
    fragmentReplacer.begin(SummaryRegisterFragment.create())
      .addToBackStack()
      .setTransition(FragmentReplacer.Transition.SRFO)
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
