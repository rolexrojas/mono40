package com.tpago.movil.dep.ui.main.recipients;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.ui.Container;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface SearchOrChooseRecipientContainer extends Container<AddRecipientComponent> {
  /**
   * TODO
   *
   * @param contact
   *   TODO
   */
  void onContactClicked(@NonNull Contact contact);

  /**
   * Creates an {@link Observable observable} that emits all query change events.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference. Unsubscribe to free this
   * reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   * <p>
   * <em>Note:</em> By default {@link #onQueryChanged()} operates on {@link
   * SchedulerProvider#ui()}.
   *
   * @return An {@link Observable observable} that emits all query change events.
   */
  @NonNull
  Observable<String> onQueryChanged();
}
