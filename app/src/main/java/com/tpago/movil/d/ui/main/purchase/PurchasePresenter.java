package com.tpago.movil.d.ui.main.purchase;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.tpago.movil.R;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.d.domain.util.Event;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.util.EventType;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.misc.rx.RxUtils;
import com.tpago.movil.d.ui.AppDialog;
import com.tpago.movil.d.ui.Presenter;
import com.tpago.movil.d.domain.ErrorCode;
import com.tpago.movil.d.domain.FailureData;
import com.tpago.movil.d.domain.Result;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.util.ObjectHelper;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * @author hecvasro
 */
@Deprecated
public final class PurchasePresenter extends Presenter<PurchaseScreen> {

  private final StringHelper stringHelper;
  private final ProductManager productManager;
  private final EventBus eventBus;
  private final AppDialog.Creator screenDialogCreator;
  private final PosBridge posBridge;

  private final NetworkService networkService;
  private final DepApiBridge depApiBridge;
  private final String phoneNumber;

  private Subscription productAdditionEventSubscription = Subscriptions.unsubscribed();
  private Subscription activationSubscription = Subscriptions.unsubscribed();

  private Product selectedProduct;

  PurchasePresenter(
    StringHelper stringHelper,
    ProductManager productManager,
    EventBus eventBus,
    AppDialog.Creator screenDialogCreator,
    PosBridge posBridge,
    NetworkService networkService,
    DepApiBridge depApiBridge,
    String phoneNumber
  ) {
    this.stringHelper = stringHelper;
    this.productManager = productManager;
    this.eventBus = eventBus;
    this.screenDialogCreator = screenDialogCreator;
    this.posBridge = posBridge;

    this.networkService = networkService;
    this.depApiBridge = depApiBridge;
    this.phoneNumber = phoneNumber;
  }

  final void start() {
    assertScreen();
    productAdditionEventSubscription = eventBus.onEventDispatched(EventType.PRODUCT_ADDITION)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<Event>() {
        @Override
        public void call(final Event event) {
          screenDialogCreator.create(stringHelper.dialogProductAdditionTitle())
            .message(stringHelper.dialogProductAdditionMessage())
            .positiveAction(
              stringHelper.dialogProductAdditionPositiveAction(),
              new AppDialog.OnActionClickedListener() {
                @Override
                public void onActionClicked(@NonNull AppDialog.Action action) {
                  eventBus.release(event);
                  screen.requestPin();
                }
              }
            )
            .negativeAction(stringHelper.dialogProductAdditionNegativeAction())
            .build()
            .show();
        }
      }, new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
          Timber.e(throwable, "Listening to creditCard addition events");
        }
      });
  }

  final void resume() {
    if (ObjectHelper.isNotNull(this.screen)) {
      boolean isListEmpty = true;
      this.screen.clearPaymentOptions();
      for (Product paymentOption : this.productManager.getPaymentOptionList()) {
        if (this.posBridge.isRegistered(paymentOption.getAltpanKey())) {
          this.screen.addPaymentOption(paymentOption);
          if (isListEmpty) {
            isListEmpty = false;
            this.screen.setHasCards(true);
          }
        }
      }
      this.selectedProduct = this.productManager.getDefaultPaymentOption();
      if (ObjectHelper.isNotNull(this.selectedProduct)) {
        this.screen.markAsSelected(this.selectedProduct);
      }
      if (isListEmpty) {
        this.screen.requestPin();
      }
    }
  }

  final void stop() {
    RxUtils.unsubscribe(this.productAdditionEventSubscription);
    RxUtils.unsubscribe(this.activationSubscription);
  }

  @NonNull
  final Product getSelectedPaymentOption() {
    return selectedProduct;
  }

  final void onPaymentOptionSelected(@NonNull Product product) {
    assertScreen();
    if (ObjectHelper.isNotNull(selectedProduct) && selectedProduct.equals(product)) {
      screen.openPaymentScreen(selectedProduct);
    } else {
      selectedProduct = product;
      screen.markAsSelected(selectedProduct);
    }
  }

  final void activateCards(final String pin) {
    if (this.activationSubscription.isUnsubscribed()) {
      this.activationSubscription = Single
        .defer(() -> {
          final Result<Boolean, ErrorCode> result;
          if (networkService.checkIfAvailable()) {
            final ApiResult<Boolean> pinValidationResult = depApiBridge.validatePin(pin);
            if (pinValidationResult.isSuccessful()) {
              if (pinValidationResult.getData()) {
                boolean flag = false;
                final StringBuilder builder = new StringBuilder();
                final List<Pair<Product, PosResult>> productRegistrationResultList
                  = productManager.registerPaymentOptionList(phoneNumber, pin);
                for (Pair<Product, PosResult> pair : productRegistrationResultList) {
                  flag |= pair.second.isSuccessful();
                  builder.append(pair.second.getData());
                  builder.append("\n");
                }
                if (flag) {
                  result = Result.create(Boolean.TRUE);
                } else {
                  result = Result.create(
                    FailureData.create(ErrorCode.UNEXPECTED, builder.toString()));
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
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          screen.onActivationFinished(result.isSuccessful());
          if (result.isSuccessful()) {
            resume();
            screen.requestNFCPermissions();
          } else {
            final FailureData<ErrorCode> failureData = result.getFailureData();
            switch (failureData.getCode()) {
              case INCORRECT_PIN:
                screen.showGenericErrorDialog(stringHelper.resolve(R.string.error_incorrect_pin));
                break;
              case UNAVAILABLE_NETWORK:
                screen.showUnavailableNetworkError();
                break;
              default:
                screen.showGenericErrorDialog(failureData.getDescription());
                break;
            }
          }
        }, throwable -> {
          Timber.e(throwable);
          screen.onActivationFinished(false);
          screen.showGenericErrorDialog();
        });
    }
  }
}
