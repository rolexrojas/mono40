package com.tpago.movil.dep.ui.main.payments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.ui.Screen;

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

  void showConfirmationDialog(
    @NonNull Recipient recipient,
    @NonNull String title,
    @Nullable String message);

  void showUnaffiliatedRecipientAdditionNotAvailableMessage();

  void showPaymentToUnaffiliatedRecipientNotAvailableMessage();

  void startTransfer(@NonNull String phoneNumber, boolean isAffiliated);

  void openIndexScreen();
  void finish();
  void setDeleting(boolean deleting);
  void startTransfer(Recipient recipient);
  void showMessage(String message);
  void remove(Object item);
  void setDeleteButtonEnabled(boolean enabled);
}
