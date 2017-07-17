package com.tpago.movil.d.domain;

import android.content.Context;

import com.tpago.movil.Session;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import rx.functions.Action1;

/**
 * @author hecvasro
 */
@Deprecated
public final class InitialDataLoader {
  private final DepApiBridge apiBridge;
  private final ProductManager productManager;
  private final RecipientManager recipientManager;
  private final Context context;

  public InitialDataLoader(
    DepApiBridge apiBridge,
    ProductManager productManager,
    RecipientManager recipientManager,
    Context context) {
    this.apiBridge = apiBridge;
    this.productManager = productManager;
    this.recipientManager = recipientManager;
    this.context = context;
  }

  @Deprecated public final Completable load(final Session session) {
    return apiBridge.initialLoad(session.getToken())
      .doOnNext(new Action1<ApiResult<InitialData>>() {
        @Override
        public void call(ApiResult<InitialData> result) {
          if (result.isSuccessful()) {
            final InitialData data = result.getData();
            productManager.syncProducts(data.getProducts());

            final List<Recipient> recipients = new ArrayList<>();
            recipients.addAll(data.getRecipients());
            for (Product product : productManager.getProductList()) {
              if (Product.checkIfCreditCard(product) || Product.checkIfLoan(product)) {
                recipients.add(new ProductRecipient(product, context.getString(ProductType.findStringId(product))));
              }
            }
            recipientManager.syncRecipients(recipients);
          }
        }
      })
      .toCompletable();
  }
}
