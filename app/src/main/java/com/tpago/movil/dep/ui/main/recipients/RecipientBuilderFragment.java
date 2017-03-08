package com.tpago.movil.dep.ui.main.recipients;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tpago.movil.Partner;
import com.tpago.movil.R;
import com.tpago.movil.dep.data.res.DepAssetProvider;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.ui.Dialogs;
import com.tpago.movil.dep.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Preconditions;
import com.tpago.movil.widget.TextInput;

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
public class RecipientBuilderFragment
  extends Fragment
  implements PinConfirmationDialogFragment.OnDismissListener {
  private static final String KEY_PIN_CONFIRMATION = "pinConfirmation";

  private static final String KEY_KEYWORD = "keyword";
  private static final String KEY_PARTNER = "partner";

  private String keyword;
  private Partner partner;
  private RecipientBuilder builder;

  private Unbinder unbinder;

  private Subscription subscription = Subscriptions.unsubscribed();

  private RecipientBuilder.Result result = null;

  @Inject SessionManager sessionManager;
  @Inject DepApiBridge apiBridge;
  @Inject DepAssetProvider assetProvider;

  public static RecipientBuilderFragment create(String keyword, Partner partner) {
    final Bundle bundle = new Bundle();
    bundle.putString(KEY_KEYWORD, keyword);
    bundle.putSerializable(KEY_PARTNER, partner);
    final RecipientBuilderFragment fragment = new RecipientBuilderFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @BindView(R.id.image_view)
  ImageView imageView;
  @BindView(R.id.text_view)
  TextView textView;
  @BindView(R.id.text_input)
  TextInput textInput;
  @BindView(R.id.button)
  Button button;

  private void resolve(RecipientBuilder.Result result) {
    this.result = result;
    final FragmentManager fm = getChildFragmentManager();
    final Fragment f = fm.findFragmentByTag(KEY_PIN_CONFIRMATION);
    if (Objects.isNotNull(f)) {
      ((PinConfirmationDialogFragment) f).resolve(this.result.isSuccessful());
    }
  }

  @OnClick(R.id.button)
  void onButtonClicked() {
    final String content = textInput.getText().toString().trim();
    if (Texts.isEmpty(content)) {
      Dialogs.builder(getContext())
        .setTitle("Número de " + keyword + " incorrecto")
        .setMessage("El número de " + keyword + " es requerido para adicionar el destinatario.")
        .setPositiveButton(R.string.ok, null)
        .create()
        .show();
      textInput.setErraticStateEnabled(true);
    } else {
      final int x = Math.round((button.getRight() - button.getLeft()) / 2);
      final int y = Math.round((button.getBottom() - button.getTop()) / 2);
      Timber.d(
        "{left:%1%d,top:%2$d,right:%3$d,bottom:%4$d",
        button.getLeft(),
        button.getTop(),
        button.getRight(),
        button.getBottom());
      Timber.d("{x:%1$d,y:%2$d}", x, y);
      PinConfirmationDialogFragment.newInstance(
        x,
        y,
        getString(R.string.recipient_addition_confirmation, content, builder.getTitle()),
        new PinConfirmationDialogFragment.Callback() {
          @Override
          public void confirm(@NonNull String pin) {
            subscription = builder.build(content, pin)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Action1<RecipientBuilder.Result>() {
                @Override
                public void call(RecipientBuilder.Result result) {
                  resolve(result);
                }
              }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                  Timber.e(throwable, "Building a recipient");
                  resolve(new RecipientBuilder.Result(getString(R.string.error_message)));
                }
              });
          }
        })
        .show(getChildFragmentManager(), KEY_PIN_CONFIRMATION);
    }
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((AddRecipientActivity) getActivity()).getComponent().inject(this);
    final Bundle bundle = Preconditions.checkNotNull(getArguments(), "getArguments() == null");
    keyword = bundle.getString(KEY_KEYWORD);
    partner = (Partner) bundle.getSerializable(KEY_PARTNER);
    builder = new BillRecipientBuilder(
      sessionManager.getSession().getAuthToken(),
      apiBridge,
      partner,
      assetProvider);
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
  }

  @Override
  public void onResume() {
    super.onResume();
    Picasso.with(getContext())
      .load(builder.getImageUri())
      .into(imageView);
    textView.setText(
      String.format(
        getString(R.string.recipient_addition_message_builder),
        keyword,
        builder.getTitle()));
    textInput.requestFocus();
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
  public void onDismiss(boolean succeeded) {
    if (Objects.isNotNull(result)) {
      if (result.isSuccessful()) {
        final Activity activity = getActivity();
        activity.setResult(
          Activity.RESULT_OK,
          AddRecipientActivity.serializeResult(result.getData()));
        activity.finish();
      } else {
        Dialogs.builder(getContext())
          .setTitle(R.string.error_title)
          .setMessage(result.getError())
          .setPositiveButton(R.string.error_positive_button_text, null)
          .create()
          .show();
      }
    }
  }
}
