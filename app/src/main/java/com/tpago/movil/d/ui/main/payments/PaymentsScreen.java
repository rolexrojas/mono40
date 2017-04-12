package com.tpago.movil.d.ui.main.payments;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.ui.Screen;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Payments screen definition.
 *
 * @author hecvasro
 */
interface PaymentsScreen extends Screen {
  /**
   * Creates an {@link Observable observable} that emits all query change events.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference. Unsubscribe to free this
   * reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   * <p>
   * <em>Note:</em> By default {@link #onQueryChanged()} operates on {@link
   * AndroidSchedulers#mainThread()}.
   *
   * @return An {@link Observable observable} that emits all query change events.
   */
  @NonNull
  Observable<String> onQueryChanged();

  void clearQuery();

  void showLoadIndicator(boolean fullscreen);

  void hideLoadIndicator();

  void clear();

  void add(@NonNull Object item);

  void update(@NonNull Object item);

  void startTransfer(Recipient recipient);

  void openInitScreen();
  void finish();
  void setDeleting(boolean deleting);
  void showMessage(String message);
  void remove(Object item);
  void setDeleteButtonEnabled(boolean enabled);

  void showRecipientAdditionDialog(Recipient recipient);
  void startNonAffiliatedPhoneNumberRecipientAddition(String phoneNumber);
  void showTransactionSummary(Recipient recipient, boolean alreadyExists, String transactionId);

  void requestPin();

  void showGenericErrorDialog(String message);
  void showGenericErrorDialog();
  void showUnavailableNetworkError();

  void setDeletingResult(boolean result);
}
