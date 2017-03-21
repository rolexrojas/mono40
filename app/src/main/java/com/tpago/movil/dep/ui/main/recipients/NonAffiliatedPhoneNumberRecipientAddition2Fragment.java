package com.tpago.movil.dep.ui.main.recipients;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.Bank;
import com.tpago.movil.R;
import com.tpago.movil.api.ApiImageUriBuilder;
import com.tpago.movil.app.App;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.api.ApiResult;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.Dialogs;
import com.tpago.movil.text.Texts;
import com.tpago.movil.widget.FullSizeLoadIndicator;
import com.tpago.movil.widget.LoadIndicator;
import com.tpago.movil.widget.TextInput;

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

  @Inject
  DepApiBridge apiBridge;
  @Inject
  SessionManager sessionManager;

  @BindView(R.id.image_view_background)
  ImageView imageView;
  @BindView(R.id.text_view)
  TextView textView;
  @BindView(R.id.text_input)
  TextInput textInput;

  @OnClick(R.id.button)
  void onButtonClicked() {
    final String content = textInput.getText().toString().trim();
    if (Texts.isEmpty(content)) {
      Dialogs.builder(getContext())
        .setTitle("Número de cuenta incorrecto")
        .setMessage("El número de cuenta es requerido para la adición del destinatario.")
        .setPositiveButton(R.string.ok, null)
        .create()
        .show();
      textInput.setErraticStateEnabled(true);
    } else {
      subscription = apiBridge.checkAccountNumber(
        sessionManager.getSession().getAuthToken(),
        recipient.getBank(),
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
              recipient.setAccountNumber(content);
              recipient.setProduct(result.getData());
              final Activity activity = getActivity();
              activity.setResult(
                Activity.RESULT_OK,
                NonAffiliatedPhoneNumberRecipientAdditionActivity.serializeResult(recipient));
              activity.finish();
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
  public void onAttach(Context context) {
    super.onAttach(context);
    recipient = ((NonAffiliatedPhoneNumberRecipientAdditionActivity) getActivity()).recipient;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((App) getActivity().getApplication()).getAppComponent().inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(
    LayoutInflater inflater,
    @Nullable ViewGroup container,
    @Nullable Bundle savedInstanceState) {
    return inflater.inflate(
      R.layout.fragment_non_affiliated_phone_number_recipient_addition_2,
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
    final Bank bank = recipient.getBank();
    Picasso.with(getContext())
      .load(ApiImageUriBuilder.build(getContext(), bank, ApiImageUriBuilder.Style.PRIMARY_24))
      .into(imageView);
    textView.setText(String.format(getString(R.string.transaction), Bank.getName(bank)));
    textInput.requestFocus();
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
