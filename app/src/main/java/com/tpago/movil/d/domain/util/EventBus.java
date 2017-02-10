package com.tpago.movil.d.domain.util;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class EventBus {
  /**
   * TODO
   */
  private final List<Event> stickyEvents = new ArrayList<>();
  /**
   * TODO
   */
  private final PublishSubject<Event> subject = PublishSubject.create();

  public EventBus() {
  }

  /**
   * TODO
   *
   * @param event
   *   TODO
   */
  public void dispatch(@NonNull Event event) {
    if (event.isSticky()) {
      if (stickyEvents.contains(event)) {
        stickyEvents.remove(event);
      }
      stickyEvents.add(0, event);
    }
    subject.onNext(event);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Event> onEventDispatched() {
    return Observable.concat(Observable.from(stickyEvents), subject.asObservable());
  }

  /**
   * TODO
   *
   * @param types
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  @SuppressWarnings("unchecked")
  public final Observable<Event> onEventDispatched(@NonNull final EventType... types) {
    return onEventDispatched()
      .filter(new Func1<Event, Boolean>() {
        @Override
        public Boolean call(Event event) {
          final EventType eventType = event.getType();
          for (EventType validType : types) {
            if (validType.equals(eventType)) {
              return true;
            }
          }
          return false;
        }
      });
  }

  /**
   * TODO
   *
   * @param event
   *   TODO
   */
  public final void release(@NonNull Event event) {
    if (stickyEvents.contains(event)) {
      stickyEvents.remove(event);
    }
  }
}
