package com.tpago.movil.dep.ui.main.transactions.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.Bank;
import com.tpago.movil.R;
import com.tpago.movil.dep.data.Formatter;
import com.tpago.movil.dep.data.res.DepAssetProvider;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.api.ApiResult;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.ChildFragment;
import com.tpago.movil.dep.ui.Dialogs;
import com.tpago.movil.dep.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.dep.ui.main.transactions.TransactionCreationComponent;
import com.tpago.movil.dep.ui.main.transactions.TransactionCreationContainer;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;
import com.tpago.movil.widget.FullSizeLoadIndicator;
import com.tpago.movil.widget.LoadIndicator;
import com.tpago.movil.widget.TextInput;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public class NonAffiliatedPhoneNumberTransactionCreation2Fragment
  extends ChildFragment<TransactionCreationContainer> {
  private static final String KEY_PIN = "pinConfirmation";

  private Unbinder unbinder;

  private LoadIndicator loadIndicator;

  private Subscription checkSubscription = Subscriptions.unsubscribed();
  private Subscription transferSubscription = Subscriptions.unsubscribed();

  @Inject
  DepApiBridge apiBridge;
  @Inject
  DepAssetProvider assetProvider;
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
      sessionManager.getSession().getAuthToken(),
      fundingAccount.get(),
      recipient,
      value.get(),
      pin)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<ApiResult<String>>() {
        @Override
        public void call(ApiResult<String> result) {
          if (result.isSuccessful()) {
            getContainer().finish(true, result.getData());
          } else {
            Dialogs.builder(getContext())
              .setTitle(R.string.error_title)
              .setMessage(result.getError().getDescription())
              .setPositiveButton(R.string.error_positive_button_text, null)
              .create()
              .show();
          }
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Transfer to a non affiliated recipient");
          Dialogs.builder(getContext())
            .setTitle(R.string.error_title)
            .setMessage(R.string.error_message)
            .setPositiveButton(R.string.error_positive_button_text, null)
            .create()
            .show();
        }
      });
  }

  @OnClick(R.id.button)
  void onButtonClicked() {
    final String content = textInput.getText().toString().trim();
    if (Texts.isEmpty(content)) {
      Dialogs.builder(getContext())
        .setTitle("Número de cuenta incorrecto")
        .setMessage("El número de cuenta es requerido para la transferencia.")
        .setPositiveButton(R.string.ok, null)
        .create()
        .show();
      textInput.setErraticStateEnabled(true);
    } else {
      final String authToken = sessionManager.getSession().getAuthToken();
      checkSubscription = apiBridge.checkAccountNumber(
        authToken,
        ((NonAffiliatedPhoneNumberRecipient) recipient).getBank(),
        content)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            loadIndicator.start();
          }
        })
        .subscribe(new Action1<ApiResult<Product>>() {
          @Override
          public void call(ApiResult<Product> result) {
            loadIndicator.stop();
            if (result.isSuccessful()) {
              ((NonAffiliatedPhoneNumberRecipient) recipient).setAccountNumber(content);
              ((NonAffiliatedPhoneNumberRecipient) recipient).setProduct(result.getData());
              final int x = Math.round((button.getRight() - button.getLeft()) / 2);
              final int y = Math.round((button.getBottom() - button.getTop()) / 2);
              PinConfirmationDialogFragment.newInstance(
                x,
                y,
                getString(R.string.format_transfer_to,
                  Formatter.amount(fundingAccount.get().getCurrency(), value.get()),
                  recipient.getIdentifier()),
                new PinConfirmationDialogFragment.Callback() {
                  @Override
                  public void confirm(@NonNull String pin) {
                    transferTo(pin);
                  }
                })
                .show(getChildFragmentManager(), KEY_PIN);
            } else {
              Dialogs.builder(getContext())
                .setTitle(R.string.error_title)
                .setMessage(result.getError().getDescription())
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
              .setTitle(R.string.error_title)
              .setMessage(R.string.error_message)
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
    if (Objects.isNotNull(c)) {
      c.inject(this);
    }
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(
      R.layout.fragment_non_affiliated_phone_number_transaction_creation_2,
      container,
      false);
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
    final Bank bank = ((NonAffiliatedPhoneNumberRecipient) recipient).getBank();
    Picasso.with(getContext())
      .load(assetProvider.getLogoUri(bank, DepAssetProvider.STYLE_24_PRIMARY))
      .into(imageView);
    textView.setText(String.format(getString(R.string.transaction), bank.getName()));
    textInput.requestFocus();
    textInput.setText(((NonAffiliatedPhoneNumberRecipient) recipient).getAccountNumber());
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
