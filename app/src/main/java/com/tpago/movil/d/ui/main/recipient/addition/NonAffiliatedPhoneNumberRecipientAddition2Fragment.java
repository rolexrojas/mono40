package com.tpago.movil.d.ui.main.recipient.addition;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.company.Company;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.R;
import com.tpago.movil.dep.App;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.dep.text.Texts;
import com.tpago.movil.dep.widget.FullSizeLoadIndicator;
import com.tpago.movil.dep.widget.Keyboard;
import com.tpago.movil.dep.widget.LoadIndicator;
import com.tpago.movil.dep.widget.TextInput;

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
public class NonAffiliatedPhoneNumberRecipientAddition2Fragment extends Fragment {

  private Unbinder unbinder;
  private NonAffiliatedPhoneNumberRecipient recipient;

  private LoadIndicator loadIndicator;

  private Subscription subscription = Subscriptions.unsubscribed();

  @Inject DepApiBridge apiBridge;
  @Inject CompanyHelper companyHelper;

  @BindView(R.id.image_view_background)
  ImageView imageView;
  @BindView(R.id.text_view)
  TextView textView;
  @BindView(R.id.text_input)
  TextInput textInput;

  @OnClick(R.id.button)
  void onButtonClicked() {
    final String content = textInput.getText()
      .toString()
      .trim();
    if (Texts.checkIfEmpty(content)) {
      Dialogs.builder(getContext())
        .setTitle("Número de cuenta incorrecto")
        .setMessage("El número de cuenta es requerido para la adición del destinatario.")
        .setPositiveButton(R.string.ok, null)
        .create()
        .show();
      textInput.setErraticStateEnabled(true);
    } else {
      subscription = apiBridge.checkAccountNumber(
        recipient.getBank(),
        content
      )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            loadIndicator.start();
          }
        })
        .subscribe(new Action1<ApiResult<Pair<String, Product>>>() {
          @Override
          public void call(ApiResult<Pair<String, Product>> result) {
            loadIndicator.stop();
            if (result.isSuccessful()) {
              final Pair<String, Product> data = result.getData();
              recipient.setLabel(data.first);
              recipient.setProduct(data.second);
              recipient.setAccountNumber(content);
              final Activity activity = getActivity();
              activity.setResult(
                Activity.RESULT_OK,
                NonAffiliatedPhoneNumberRecipientAdditionActivityBase.serializeResult(recipient)
              );
              activity.finish();
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
  public void onAttach(Context context) {
    super.onAttach(context);
    recipient = ((NonAffiliatedPhoneNumberRecipientAdditionActivityBase) getActivity()).recipient;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    App.get(getContext())
      .component()
      .inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState
  ) {
    return inflater.inflate(
      R.layout.d_fragment_non_affiliated_phone_number_recipient_addition_2,
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
    final Bank bank = recipient.getBank();
    Picasso.with(getContext())
      .load(companyHelper.getLogoUri(bank, Company.LogoStyle.COLORED_24))
      .noFade()
      .into(imageView);

    this.textView.setText(
      this.getString(
        R.string.accountNumberConfirmationMessage,
        this.getString(R.string.input),
        bank.name()
      )
    );

    Keyboard.show(textInput);
    textInput.setText(recipient.getAccountNumber());
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
    if (!subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    recipient = null;
  }
}
