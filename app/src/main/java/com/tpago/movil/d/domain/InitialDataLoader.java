package com.tpago.movil.d.domain;

import android.content.Context;

import com.tpago.movil.d.data.api.CustomerSecretKey;
import com.tpago.movil.d.data.api.CustomerSecretTokenResponse;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;

/**
 * @author hecvasro
 */
@Deprecated
public final class InitialDataLoader {

    private final DepApiBridge apiBridge;
    private final ProductManager productManager;
    private final RecipientManager recipientManager;
    private SessionManager sessionManager;
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
            final List<Recipient> recipients = new ArrayList<>(data.getRecipients());
            for (Product product : productManager.getProductList()) {
                if (Product.checkIfCreditCard(product) || Product.checkIfLoan(product)) {
                    recipients.add(
                            new ProductRecipient(
                                    product,
                                    context.getString(ProductType.findStringId(product))
                            )
                    );
                }
            }
            recipientManager.syncRecipients(recipients);
        }
    }

    private void handleCustomerSecretResult(ApiResult<CustomerSecretTokenResponse> customerSecretTokenResponseApiResult) {
        if (customerSecretTokenResponseApiResult.isSuccessful()) {
            sessionManager.saveCustomerSecretToken(customerSecretTokenResponseApiResult.getData().token());
        }
    }

    private void handleCustomerSecretKeyResult(ApiResult<CustomerSecretKey> customerSecretKeyResponseApiResult) {
        if (customerSecretKeyResponseApiResult.isSuccessful()) {
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
