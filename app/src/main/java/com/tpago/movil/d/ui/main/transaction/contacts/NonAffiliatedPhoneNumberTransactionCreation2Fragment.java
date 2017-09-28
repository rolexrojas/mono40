package com.tpago.movil.d.ui.main.transaction.contacts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.d.domain.Banks;
import com.tpago.movil.dep.api.DCurrencies;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.R;
import com.tpago.movil.d.data.Formatter;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.ChildFragment;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationComponent;
import com.tpago.movil.d.ui.main.transaction.TransactionCreationContainer;
import com.tpago.movil.d.domain.LogoStyle;
import com.tpago.movil.dep.text.Texts;
import com.tpago.movil.dep.Objects;
import com.tpago.movil.dep.widget.FullSizeLoadIndicator;
import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.dep.widget.LoadIndicator;
import com.tpago.movil.dep.widget.TextInput;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberTransactionCreation2Fragment extends
  ChildFragment<TransactionCreationContainer> {

  private Unbinder unbinder;

  private LoadIndicator loadIndicator;

  private Subscription checkSubscription = Subscriptions.unsubscribed();
  private Subscription transferSubscription = Subscriptions.unsubscribed();

  @Inject
  DepApiBridge apiBridge;
  @Inject
  SessionManager sessionManager;
  @Inject
  Recipient recipient;
  @Inject
  AtomicReference<Product> fundingAccount;
  @Inject
  AtomicReference<BigDecimal> value;

  @BindView(R.id.image_view_background)
  ImageView imageView;
  @BindView(R.id.text_view)
  TextView textView;
  @BindView(R.id.text_input)
  TextInput textInput;
  @BindView(R.id.button)
  Button button;

  private void transferTo(String pin) {
    transferSubscription = apiBridge.transferTo(
      sessionManager.getSession()
        .getAuthToken(),
      fundingAccount.get(),
      recipient,
      value.get(),
      pin
    )
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<ApiResult<String>>() {
        @Override
        public void call(ApiResult<String> result) {
          PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), result.isSuccessful());
          if (result.isSuccessful()) {
            getContainer().finish(true, result.getData());
          } else {
            Dialogs.builder(getContext())
              .setTitle(R.string.error_generic_title)
              .setMessage(result.getError()
                .getDescription())
              .setPositiveButton(R.string.error_positive_button_text, null)
              .create()
              .show();
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Transfer to a non affiliated recipient");
          PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), false);
          Dialogs.builder(getContext())
            .setTitle(R.string.error_generic_title)
            .setMessage(R.string.error_generic)
            .setPositiveButton(R.string.error_positive_button_text, null)
            .create()
            .show();
        }
      });
  }

  @OnClick(R.id.button)
  void onButtonClicked() {
    final String content = textInput.getText()
      .toString()
      .trim();
    if (Texts.checkIfEmpty(content)) {
      Dialogs.builder(getContext())
        .setTitle("Número de cuenta incorrecto")
        .setMessage("El número de cuenta es requerido para la transferencia.")
        .setPositiveButton(R.string.ok, null)
        .create()
        .show();
      textInput.setErraticStateEnabled(true);
    } else {
      final String authToken = sessionManager.getSession()
        .getAuthToken();
      final Bank bank;
      if (recipient instanceof AccountRecipient) {
        bank = ((AccountRecipient) recipient).bank();
      } else {
        bank = ((NonAffiliatedPhoneNumberRecipient) recipient).getBank();
      }
      checkSubscription = apiBridge.checkAccountNumber(authToken, bank, content)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(this.loadIndicator::start)
        .subscribe(new Action1<ApiResult<Pair<String, Product>>>() {
          @Override
          public void call(ApiResult<Pair<String, Product>> result) {
            loadIndicator.stop();
            if (result.isSuccessful()) {
              final Pair<String, Product> data = result.getData();
              if (recipient instanceof AccountRecipient) {
                final AccountRecipient r = (AccountRecipient) recipient;
                r.setLabel(data.first);
                r.product(data.second);
                r.number(content);
              } else {
                final NonAffiliatedPhoneNumberRecipient r
                  = (NonAffiliatedPhoneNumberRecipient) recipient;
                r.setLabel(data.first);
                r.setProduct(data.second);
                r.setAccountNumber(content);
              }
              final int x = Math.round((button.getRight() - button.getLeft()) / 2);
              final int y = Math.round((button.getBottom() - button.getTop()) / 2);
              PinConfirmationDialogFragment.show(
                getChildFragmentManager(),
                getString(
                  R.string.format_transfer_to,
                  Formatter
                    .amount(DCurrencies.map(fundingAccount.get()
                      .getCurrency()), value.get()),
                  data.first,
                  Formatter.amount(
                    DCurrencies.map(fundingAccount.get()
                      .getCurrency()),
                    Bank.calculateTransferCost(value.get())
                  )
                ),
                new PinConfirmationDialogFragment.Callback() {
                  @Override
                  public void confirm(String pin) {
                    transferTo(pin);
                  }
                },
                x,
                y
              );
            } else {
              Dialogs.builder(getContext())
                .setTitle(R.string.error_generic_title)
                .setMessage(result.getError()
                  .getDescription())
                .setPositiveButton(R.string.error_positive_button_text, null)
                .create()
                .show();
            }
          }
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "");
            loadIndicator.stop();
            Dialogs.builder(getContext())
              .setTitle(R.string.error_generic_title)
              .setMessage(R.string.error_generic)
              .setPositiveButton(R.string.error_positive_button_text, null)
              .create()
              .show();
          }
        });
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final TransactionCreationComponent c = getContainer().getComponent();
    if (Objects.checkIfNotNull(c)) {
      c.inject(this);
    }
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(
      R.layout.d_fragment_non_affiliated_phone_number_transaction_creation_2,
      container,
      false
    );
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    loadIndicator = new FullSizeLoadIndicator(getChildFragmentManager());
  }

  @Override
  public void onResume() {
    super.onResume();
    final Bank bank;
    final String number;
    if (recipient instanceof AccountRecipient) {
      final AccountRecipient r = (AccountRecipient) recipient;

      bank = r.bank();
      number = r.number();
    } else {
      final NonAffiliatedPhoneNumberRecipient r = (NonAffiliatedPhoneNumberRecipient) recipient;

      bank = r.getBank();
      number = r.getAccountNumber();
    }
    Picasso.with(getContext())
      .load(bank.getLogoUri(LogoStyle.PRIMARY_24))
      .noFade()
      .into(imageView);
    textView.setText(String.format(getString(R.string.transaction), Banks.getName(bank)));
    Keyboard.show(textInput);
    textInput.setText(number);
    textInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          onButtonClicked();
        }
        return false;
      }
    });
  }

  @Override
  public void onPause() {
    super.onPause();
    if (!transferSubscription.isUnsubscribed()) {
      transferSubscription.unsubscribe();
    }
    if (!checkSubscription.isUnsubscribed()) {
      checkSubscription.unsubscribe();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
