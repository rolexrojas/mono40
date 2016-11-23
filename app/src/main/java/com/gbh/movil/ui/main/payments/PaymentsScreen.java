package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.Screen;

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

  /**
   * TODO
   */
  void clearQuery();

  /**
   * TODO
   *
   * @param fullscreen
   *   TODO
   */
  void showLoadIndicator(boolean fullscreen);

  /**
   * TODO
   */
  void hideLoadIndicator();

  /**
   * TODO
   */
  void clear();

  /**
   * TODO
   *
   * @param item
   *   TODO
   */
  void add(@NonNull Object item);

  /**
   * TODO
   *
   * @param item
   *   TODO
   */
  void update(@NonNull Object item);

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   */
  void showRecipientAdditionConfirmationDialog(@NonNull Recipient recipient);

  /**
   * TODO
   */
  void showUnaffiliatedRecipientAdditionNotAvailableMessage();
}
