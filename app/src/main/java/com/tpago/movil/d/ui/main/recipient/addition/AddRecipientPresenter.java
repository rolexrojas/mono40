package com.tpago.movil.d.ui.main.recipient.addition;

import androidx.annotation.NonNull;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.loader.takeover.TakeoverLoader;
import com.tpago.movil.d.domain.Customer;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.reactivex.DisposableUtil;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author hecvasro
 */
final class AddRecipientPresenter extends Presenter<AddRecipientScreen> {

    private final RecipientManager recipientManager;

    private final DepApiBridge apiBridge;
    private final AlertManager alertManager;
    private final StringMapper stringMapper;
    private final TakeoverLoader takeoverLoader;
    private Contact selectedContact;

    private Disposable disposable = Disposables.disposed();

    AddRecipientPresenter(
            RecipientManager recipientManager,
            DepApiBridge apiBridge,
            AlertManager alertManager,
            StringMapper stringMapper,
            TakeoverLoader takeoverLoader
    ) {
        this.recipientManager = ObjectHelper
                .checkNotNull(recipientManager, "recipientManager");

        this.apiBridge = ObjectHelper.checkNotNull(apiBridge, "apiBridge");

        this.alertManager = ObjectHelper.checkNotNull(alertManager, "alertManager");
        this.stringMapper = ObjectHelper.checkNotNull(stringMapper, "stringMapper");
        this.takeoverLoader = ObjectHelper.checkNotNull(takeoverLoader, "takeoverLoader");
    }

    private void handleResult(ApiResult<Customer> customerResult) {
        final PhoneNumber recipientPhoneNumber = selectedContact.phoneNumber();
        final Recipient recipient;
        if (customerResult != null && customerResult.isSuccessful()) {
            final String recipientLabel = customerResult.getData().getName();
            recipient = new PhoneNumberRecipient(recipientPhoneNumber, recipientLabel);
        } else {
            final String recipientLabel = selectedContact.name();
            recipient = new NonAffiliatedPhoneNumberRecipient(recipientPhoneNumber, recipientLabel);
        }
        this.recipientManager.add(recipient);
        this.screen.finish(recipient);
    }

    private void handleError(Throwable throwable) {
        Timber.e(throwable, "Adding contact recipient");
        this.alertManager.showAlertForGenericFailure();
    }

    final void add(@NonNull final Contact contact) {
        final String phoneNumber = contact.phoneNumber()
                .value();
        this.selectedContact = contact;
        this.disposable = Single.defer(() -> Single.just(this.apiBridge.fetchCustomer(phoneNumber)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((disposable) -> this.takeoverLoader.show())
                .doFinally(this.takeoverLoader::hide)
                .subscribe(this::handleResult, this::handleError);
    }

    final void onStop() {
        DisposableUtil.dispose(this.disposable);
    }
}
