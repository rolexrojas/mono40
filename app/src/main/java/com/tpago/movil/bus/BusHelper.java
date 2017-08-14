package com.tpago.movil.bus;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import org.greenrobot.eventbus.EventBus;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Hector Vasquez
 */
public final class BusHelper {

  private static final ImmutableMap<Class<?>, Class<?>> TYPE_LIST;

  static {
    TYPE_LIST = new ImmutableMap.Builder<Class<?>, Class<?>>()
      .build();
  }

  @SuppressWarnings("unchecked")
  public static <T> Optional<T> findSticky(EventBus eventBus, Class<T> eventType) {
    checkNotNull(eventBus, "eventBus == null");
    checkNotNull(eventType, "eventType == null");

    final Class<T> type;
    if (TYPE_LIST.containsKey(eventType)) {
      type = (Class<T>) TYPE_LIST.get(eventType);
    } else {
      type = eventType;
    }

    return Optional.fromNullable(eventBus.getStickyEvent(type));
  }

  public static <T> Optional<T> findStickyAndRepost(EventBus eventBus, Class<T> eventType) {
    checkNotNull(eventBus, "eventBus == null");
    checkNotNull(eventType, "eventType == null");

    final Optional<T> stickyEvent = findSticky(eventBus, eventType);
    if (stickyEvent.isPresent()) {
      eventBus.postSticky(stickyEvent.get());
    }
    return stickyEvent;
  }

  public static <T> Optional<T> findStickyAndRemove(EventBus eventBus, Class<T> eventType) {
    checkNotNull(eventBus, "eventBus == null");
    checkNotNull(eventType, "eventType == null");

    final Optional<T> stickyEvent = findSticky(eventBus, eventType);
    if (stickyEvent.isPresent()) {
      eventBus.removeStickyEvent(stickyEvent.get());
    }
    return stickyEvent;
  }

  @SuppressWarnings("unchecked")
  public static <T> Optional<T> findStickyAndRemove(EventBus eventBus, T event) {
    checkNotNull(eventBus, "eventBus == null");
    checkNotNull(event, "event == null");

    return findStickyAndRemove(eventBus, (Class<T>) event.getClass());
  }

  private BusHelper() {
  }
}
