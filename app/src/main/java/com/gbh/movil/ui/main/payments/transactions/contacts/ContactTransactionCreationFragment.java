package com.gbh.movil.ui.main.payments.transactions.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.gbh.movil.R;
import com.gbh.movil.domain.Product;
import com.gbh.movil.ui.SubFragment;
import com.gbh.movil.ui.UiUtils;
import com.gbh.movil.ui.main.payments.transactions.PaymentOptionAdapter;
import com.gbh.movil.ui.main.payments.transactions.TransactionCreationContainer;

import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ContactTransactionCreationFragment extends SubFragment<TransactionCreationContainer>
  implements PhoneNumberTransactionCreationScreen {
  @Inject
  PhoneNumberTransactionCreationPresenter presenter;

  private Unbinder unbinder;

  private PaymentOptionAdapter paymentOptionAdapter;

  @BindView(R.id.transaction_creation_payment_option_chooser)
  Spinner paymentOptionChooser;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public static ContactTransactionCreationFragment newInstance() {
    return new ContactTransactionCreationFragment();
  }

  @OnClick(R.id.action_recharge)
  void onRechargeButtonClicked() {
    UiUtils.createDialog(getContext(), getString(R.string.sorry),
      getString(R.string.info_not_available_recharge), getString(R.string.ok), null, null, null)
      .show();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final ContactTransactionCreationComponent component = DaggerContactTransactionCreationComponent
      .builder()
      .transactionCreationComponent(container.getComponent())
      .build();
    component.inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_transaction_creation_phone_number, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this, view);
    // Prepares the list of payment options.
    paymentOptionAdapter = new PaymentOptionAdapter(getContext());
    paymentOptionChooser.setAdapter(paymentOptionAdapter);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    // Starts the presenter.
    presenter.start();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Stops the presenter.
    presenter.stop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    // Detaches the screen from the presenter.
    presenter.detachScreen();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
  }

  @Override
  public void setPaymentOptions(@NonNull Set<Product> paymentOptions) {
    paymentOptionAdapter.addAll(paymentOptions);
  }
}
