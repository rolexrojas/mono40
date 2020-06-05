package com.mono40.movil.d.ui.main.recipient.addition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
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
import com.mono40.movil.app.ui.loader.takeover.TakeoverLoaderDialogFragment;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.R;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.d.domain.api.ApiResult;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.ui.Dialogs;
import com.mono40.movil.d.ui.main.PinConfirmationDialogFragment;
import com.mono40.movil.d.domain.ErrorCode;
import com.mono40.movil.d.domain.FailureData;
import com.mono40.movil.d.domain.Result;
import com.mono40.movil.dep.init.InitActivityBase;
import com.mono40.movil.dep.net.NetworkService;
import com.mono40.movil.dep.reactivex.Disposables;
import com.mono40.movil.dep.text.Texts;
import com.mono40.movil.dep.widget.TextInput;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.util.ObjectHelper;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
public class RecipientBuilderFragment extends Fragment {

    private static final String KEY_KEYWORD = "keyword";
    private static final String KEY_DATA = "value";
    private static final String KEY_DATA_TYPE = "accountType";
    private static final String TAKE_OVER_LOADER_DIALOG = "TAKE_OVER_LOADER";


    private String keyword;
    private String accountType;
    private Parcelable data;
    private RecipientBuilder builder;

    private Unbinder unbinder;

    private Disposable subscription = Disposables.disposed();

    @Inject
    DepApiBridge apiBridge;
    @Inject
    NetworkService networkService;
    @Inject
    CompanyHelper companyHelper;
    @Inject
    SessionManager sessionManager;
    private TakeoverLoaderDialogFragment takeoverLoader;
    private Disposable closeSessionDisposable;

    private static RecipientBuilderFragment internalCreate(String keyword, Parcelable data) {
        final Bundle bundle = new Bundle();
        bundle.putString(KEY_KEYWORD, keyword);
        bundle.putParcelable(KEY_DATA, data);
        final RecipientBuilderFragment fragment = new RecipientBuilderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private static RecipientBuilderFragment internalCreate(String keyword, Parcelable data, String accountType) {
        final Bundle bundle = new Bundle();
        bundle.putString(KEY_KEYWORD, keyword);
        bundle.putString(KEY_DATA_TYPE, accountType);
        bundle.putParcelable(KEY_DATA, data);
        final RecipientBuilderFragment fragment = new RecipientBuilderFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RecipientBuilderFragment create(String keyword, Bank data) {
        return internalCreate(keyword, data);
    }

    public static RecipientBuilderFragment create(String keyword, Bank data, String accountType) {
        return internalCreate(keyword, data, accountType);
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
                                        final ApiResult<Boolean> pinValidationResult = apiBridge.validatePin(pin);
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
                                                        AddRecipientActivityBase.serializeResult(result.getSuccessData())
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
                                                        showGenericErrorDialog(failureData.getDescription());
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
                .setPositiveButton(R.string.error_positive_button_text, (dialog, which) -> {
                    if (message.contains(getString(R.string.session_expired))) {
                        closeSession();
                    }
                })
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
        ((AddRecipientActivityBase) getActivity()).getComponent()
                .inject(this);
        final Bundle bundle = this.getArguments();
        keyword = bundle.getString(KEY_KEYWORD);
        accountType = bundle.getString(KEY_DATA_TYPE);
        data = bundle.getParcelable(KEY_DATA);
        if (data instanceof Partner) {
            builder = new BillRecipientBuilder(
                    apiBridge,
                    (Partner) data,
                    this.companyHelper
            );
        } else {
            if (ObjectHelper.isNotNull(accountType)) {
                builder = new ProductRecipientBuilder(
                        apiBridge,
                        (Bank) data,
                        this.companyHelper,
                        accountType
                );
            } else {
                builder = new ProductRecipientBuilder(
                        apiBridge,
                        (Bank) data,
                        this.companyHelper
                );
            }
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
        Picasso.get()
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

    private void closeSession() {
        this.closeSessionDisposable = sessionManager.closeSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> this.showTakeOver())
                .doFinally(this::dismissTakeOverLoader)
                .subscribe(this::handleCloseSession, (Consumer<Throwable>) throwable -> {
                    Log.d("com.cryptoqr.mobile", throwable.getMessage(), throwable);
                });
    }

    private void showTakeOver() {
        if (takeoverLoader != null) {
            takeoverLoader.dismiss();
        } else {
            takeoverLoader = TakeoverLoaderDialogFragment.create();
            getChildFragmentManager().beginTransaction()
                    .add(takeoverLoader, TAKE_OVER_LOADER_DIALOG)
                    .show(takeoverLoader)
                    .commit();
        }

    }

    private void dismissTakeOverLoader() {
        if (takeoverLoader != null) {
            takeoverLoader.dismiss();
            takeoverLoader = null;
        }
    }

    private void handleCloseSession() {
        Intent intent = InitActivityBase.getLaunchIntent(getContext());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.getActivity().finish();
        this.startActivity(intent);
    }
}
