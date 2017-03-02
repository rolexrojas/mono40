package com.tpago.movil.dep.ui.main.recipients;

import android.app.Activity;
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
import com.tpago.movil.R;
import com.tpago.movil.dep.ui.Dialogs;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Preconditions;
import com.tpago.movil.widget.FullSizeLoadIndicator;
import com.tpago.movil.widget.LoadIndicator;
import com.tpago.movil.widget.TextInput;

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
public class RecipientBuilderFragment extends Fragment {
  private static final String KEY_KEYWORD = "keyword";
  private static final String KEY_BUILDER = "builder";

  private String keyword;
  private RecipientBuilder builder;

  private Unbinder unbinder;
  private LoadIndicator loadIndicator;

  private Subscription subscription = Subscriptions.unsubscribed();

  public static RecipientBuilderFragment create(String keyword, RecipientBuilder builder) {
    final Bundle bundle = new Bundle();
    bundle.putString(KEY_KEYWORD, keyword);
    bundle.putSerializable(KEY_BUILDER, builder);
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
      subscription = builder.build(content)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            loadIndicator.start();
          }
        })
        .subscribe(new Action1<RecipientBuilder.Result>() {
          @Override
          public void call(RecipientBuilder.Result result) {
            loadIndicator.stop();
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
        }, new Action1<Throwable>() {
          @Override
          public void call(Throwable throwable) {
            Timber.e(throwable, "Building a recipient");
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
    final Bundle bundle = Preconditions.checkNotNull(getArguments(), "getArguments() == null");
    keyword = bundle.getString(KEY_KEYWORD);
    builder = (RecipientBuilder) bundle.getSerializable(KEY_BUILDER);
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
    Picasso.with(getContext())
      .load(builder.getImagePath())
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
}
