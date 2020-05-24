package com.mono40.movil.d.domain;

import android.content.Context;

import com.mono40.movil.d.data.api.CustomerSecretKey;
import com.mono40.movil.d.data.api.CustomerSecretTokenResponse;
import com.mono40.movil.d.domain.api.ApiResult;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Completable;

/**
 * @author hecvasro
 */
@Deprecated
public final class InitialDataLoader {

    private final DepApiBridge apiBridge;
    private final ProductManager productManager;
    private final RecipientManager recipientManager;
    @Inject
    public SessionManager sessionManager;
    private final Context context;

    public InitialDataLoader(
            DepApiBridge apiBridge,
            ProductManager productManager,
            RecipientManager recipientManager,
            Context context
    ) {
        this.apiBridge = apiBridge;
        this.productManager = productManager;
        this.recipientManager = recipientManager;
        this.context = context;
    }

    private void handleInitialLoadResult(ApiResult<InitialData> result) {
        if (result.isSuccessful()) {
            final InitialData data = result.getData();
            productManager.syncProducts(data.getProducts());
            final List<Recipient> recipients = new ArrayList<>();
           /* for (Product product : productManager.getProductList()) {
                if (Product.checkIfCreditCard(product) || Product.checkIfLoan(product)) {
                    recipients.add(
                            new ProductRecipient(
                                    product,
                                    context.getString(ProductType.findStringId(product))
                            )
                    );
                }
            }
         */   recipientManager.syncRecipients(recipients);
        }
    }

    private void handleCustomerSecretResult(ApiResult<CustomerSecretTokenResponse> customerSecretTokenResponseApiResult) {
        if (customerSecretTokenResponseApiResult.isSuccessful() && sessionManager != null) {
            sessionManager.saveCustomerSecretToken(customerSecretTokenResponseApiResult.getData().token());
        }
    }

    private void handleCustomerSecretKeyResult(ApiResult<CustomerSecretKey> customerSecretKeyResponseApiResult) {
        if (customerSecretKeyResponseApiResult.isSuccessful() && sessionManager != null) {
            sessionManager.saveCustomerSecretKey(customerSecretKeyResponseApiResult.getData().key());
        }
    }

    @Deprecated
    public final Completable load(SessionManager sessionManager) {
        if (sessionManager != null) {
            this.sessionManager = sessionManager;
        }
        return apiBridge.initialLoad()
                .doOnNext(this::handleInitialLoadResult)
                .toCompletable()
                .concatWith(apiBridge
                        .fetchCustomerSecretToken()
                        .doOnNext(this::handleCustomerSecretResult)
                        .toCompletable())
                .concatWith(apiBridge.fetchCustomerSecretKey()
                        .doOnNext(this::handleCustomerSecretKeyResult)
                        .toCompletable());
    }
}
