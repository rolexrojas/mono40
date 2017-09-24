package com.tpago.movil.d.ui.main.recipient.addition;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tpago.movil.Partner;
import com.tpago.movil.R;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.ui.Dialogs;
import com.tpago.movil.d.ui.main.PinConfirmationDialogFragment;
import com.tpago.movil.domain.Bank;
import com.tpago.movil.domain.ErrorCode;
import com.tpago.movil.domain.FailureData;
import com.tpago.movil.domain.Result;
import com.tpago.movil.net.NetworkService;
import com.tpago.movil.reactivex.Disposables;
import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Preconditions;
import com.tpago.movil.widget.TextInput;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public class RecipientBuilderFragment extends Fragment {

  private static final String KEY_KEYWORD = "keyword";
  private static final String KEY_DATA = "data";

  private String keyword;
  private Parcelable data;
  private RecipientBuilder builder;

  private Unbinder unbinder;

  private Disposable subscription = Disposables.disposed();

  @Inject
  SessionManager sessionManager;
  @Inject
  DepApiBridge apiBridge;
  @Inject
  NetworkService networkService;

  private static RecipientBuilderFragment internalCreate(String keyword, Parcelable data) {
    final Bundle bundle = new Bundle();
    bundle.putString(KEY_KEYWORD, keyword);
    bundle.putParcelable(KEY_DATA, data);
    final RecipientBuilderFragment fragment = new RecipientBuilderFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  public static RecipientBuilderFragment create(String keyword, Bank data) {
    return internalCreate(keyword, data);
  }

  public static RecipientBuilderFragment create(String keyword, Partner data) {
    return internalCreate(keyword, data);
  }

  @BindView(R.id.image_view_background)
  ImageView imageView;
  @BindView(R.id.text_view)
  TextView textView;
  @BindView(R.id.text_input)
  TextInput textInput;
  @BindView(R.id.button)
  Button button;

  @OnClick(R.id.button)
  void onButtonClicked() {
    final String content = textInput.getText()
      .toString()
      .trim();
    if (Texts.checkIfEmpty(content)) {
      showGenericErrorDialog(
        "Número de " + keyword + " incorrecto",
        "El número de " + keyword + " es requerido para adicionar el destinatario."
      );
      textInput.setErraticStateEnabled(true);
    } else {
      final int x = Math.round((button.getRight() - button.getLeft()) / 2);
      final int y = Math.round((button.getBottom() - button.getTop()) / 2);
      PinConfirmationDialogFragment.show(
        getChildFragmentManager(),
        getString(
          R.string.recipient_addition_confirmation,
          builder.getCategoryName(),
          content,
          builder.getTitle()
        ),
        new PinConfirmationDialogFragment.Callback() {
          @Override
          public void confirm(final String pin) {
            subscription = Single.defer(new Callable<SingleSource<Result<Recipient, ErrorCode>>>() {
              @Override
              public SingleSource<Result<Recipient, ErrorCode>> call() throws Exception {
                final Result<Recipient, ErrorCode> result;
                if (networkService.checkIfAvailable()) {
                  final String authToken = sessionManager.getSession()
                    .getAuthToken();
                  final ApiResult<Boolean> pinValidationResult = apiBridge.validatePin(
                    authToken,
                    pin
                  );
                  if (pinValidationResult.isSuccessful()) {
                    if (pinValidationResult.getData()) {
                      final RecipientBuilder.Result builderResult = builder.build(content, pin)
                        .toBlocking()
                        .single();
                      if (builderResult.isSuccessful()) {
                        result = Result.create(builderResult.getData());
                      } else {
                        result = Result.create(
                          FailureData.create(
                            ErrorCode.UNEXPECTED,
                            builderResult.getError()
                          ));
                      }
                    } else {
                      result = Result.create(FailureData.create(ErrorCode.INCORRECT_PIN));
                    }
                  } else {
                    result = Result.create(
                      FailureData.create(
                        ErrorCode.UNEXPECTED,
                        pinValidationResult.getError()
                          .getDescription()
                      ));
                  }
                } else {
                  result = Result.create(FailureData.create(ErrorCode.UNAVAILABLE_NETWORK));
                }
                return Single.just(result);
              }
            })
              .subscribeOn(io.reactivex.schedulers.Schedulers.io())
              .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
              .subscribe(new Consumer<Result<Recipient, ErrorCode>>() {
                @Override
                public void accept(Result<Recipient, ErrorCode> result) throws Exception {
                  final FragmentManager fragmentManager = getChildFragmentManager();
                  if (result.isSuccessful()) {
                    PinConfirmationDialogFragment.dismiss(fragmentManager, true);
                    final Activity activity = getActivity();
                    activity.setResult(
                      Activity.RESULT_OK,
                      AddRecipientActivity.serializeResult(result.getSuccessData())
                    );
                    activity.finish();
                  } else {
                    PinConfirmationDialogFragment.dismiss(fragmentManager, false);
                    final FailureData<ErrorCode> failureData = result.getFailureData();
                    switch (failureData.getCode()) {
                      case INCORRECT_PIN:
                        showGenericErrorDialog(getString(R.string.error_incorrect_pin));
                        break;
                      case UNAVAILABLE_NETWORK:
                        showGenericErrorDialog(getString(R.string.error_unavailable_network));
                        break;
                      default:
                        showGenericErrorDialog();
                        break;
                    }
                  }
                }
              }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                  Timber.e(throwable);
                  PinConfirmationDialogFragment.dismiss(getChildFragmentManager(), false);
                  showGenericErrorDialog();
                }
              });
          }
        },
        x,
        y
      );
    }
  }

  public void showGenericErrorDialog(String title, String message) {
    Dialogs.builder(getContext())
      .setTitle(title)
      .setMessage(message)
      .setPositiveButton(R.string.error_positive_button_text, null)
      .show();
  }

  public void showGenericErrorDialog(String message) {
    showGenericErrorDialog(getString(R.string.error_generic_title), message);
  }

  public void showGenericErrorDialog() {
    showGenericErrorDialog(getString(R.string.error_generic));
  }

  public void showUnavailableNetworkError() {
    Toast.makeText(getContext(), R.string.error_unavailable_network, Toast.LENGTH_LONG)
      .show();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((AddRecipientActivity) getActivity()).getComponent()
      .inject(this);
    final Bundle bundle = Preconditions.assertNotNull(getArguments(), "getArguments() == null");
    keyword = bundle.getString(KEY_KEYWORD);
    data = bundle.getParcelable(KEY_DATA);
    if (data instanceof Partner) {
      builder = new BillRecipientBuilder(
        sessionManager.getSession()
          .getAuthToken(),
        apiBridge,
        (Partner) data
      );
    } else {
      builder = new ProductRecipientBuilder(
        sessionManager.getSession()
          .getAuthToken(),
        apiBridge,
        (Bank) data
      );
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
      R.layout.d_fragment_non_affiliated_phone_number_recipient_addition_2,
      container,
      false
    );
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
      .load(builder.getImageUri(getContext()))
      .into(imageView);
    textView.setText(
      String.format(
        getString(R.string.recipient_addition_message_builder),
        keyword,
        builder.getTitle()
      ));
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
    Disposables.dispose(subscription);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
