package com.tpago.movil.ui.main.recipients;

import android.support.annotation.NonNull;

import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.ui.Refreshable;
import com.tpago.movil.ui.Screen;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface RecipientCandidateListScreen extends Screen, Refreshable {
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
}
