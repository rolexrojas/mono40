package com.tpago.movil.d.ui.main.recipients;

import android.support.annotation.NonNull;

import com.tpago.movil.Partner;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.ui.Container;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface SearchOrChooseRecipientContainer extends Container<AddRecipientComponent> {
  void onContactClicked(Contact contact);
  void onPartnerClicked(Partner partner);

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
