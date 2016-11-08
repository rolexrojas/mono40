package com.gbh.movil.ui.main.payments;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Recipient;
import com.gbh.movil.ui.Refreshable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Payments screen definition.
 *
 * @author hecvasro
 */
interface PaymentsScreen extends Refreshable {
  /**
   * TODO
   */
  void clear();

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   */
  void add(@NonNull Recipient recipient);

  /**
   * TODO
   *
   * @param action
   *   TODO
   */
  void add(@NonNull Action action);

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
}
