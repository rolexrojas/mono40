package com.tpago.movil.data.bus;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.tpago.movil.util.ObjectHelper;

import org.greenrobot.eventbus.EventBus;

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
    ObjectHelper.checkNotNull(eventBus, "eventBus");
    ObjectHelper.checkNotNull(eventType, "eventType");

    final Class<T> type;
    if (TYPE_LIST.containsKey(eventType)) {
      type = (Class<T>) TYPE_LIST.get(eventType);
    } else {
      type = eventType;
    }

    return Optional.fromNullable(eventBus.getStickyEvent(type));
  }

  public static <T> Optional<T> findStickyAndRepost(EventBus eventBus, Class<T> eventType) {
    ObjectHelper.checkNotNull(eventBus, "eventBus");
    ObjectHelper.checkNotNull(eventType, "eventType");

    final Optional<T> stickyEvent = findSticky(eventBus, eventType);
    if (stickyEvent.isPresent()) {
      eventBus.postSticky(stickyEvent.get());
    }
    return stickyEvent;
  }

  public static <T> Optional<T> findStickyAndRemove(EventBus eventBus, Class<T> eventType) {
    ObjectHelper.checkNotNull(eventBus, "eventBus");
    ObjectHelper.checkNotNull(eventType, "eventType");

    final Optional<T> stickyEvent = findSticky(eventBus, eventType);
    if (stickyEvent.isPresent()) {
      eventBus.removeStickyEvent(stickyEvent.get());
    }
    return stickyEvent;
  }

  @SuppressWarnings("unchecked")
  public static <T> Optional<T> findStickyAndRemove(EventBus eventBus, T event) {
    ObjectHelper.checkNotNull(eventBus, "eventBus");
    ObjectHelper.checkNotNull(event, "event");

    return findStickyAndRemove(eventBus, (Class<T>) event.getClass());
  }

  private BusHelper() {
  }
}
