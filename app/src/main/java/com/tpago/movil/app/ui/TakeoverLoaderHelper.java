package com.tpago.movil.app.ui;

import com.tpago.movil.util.ObjectHelper;

import org.greenrobot.eventbus.EventBus;

import rx.functions.Action0;

/**
 * @author hecvasro
 */
public final class TakeoverLoaderHelper {

  public static Action0 createShowEventAction(EventBus eventBus) {
    ObjectHelper.checkNotNull(eventBus, "eventBus == null");
    return () -> eventBus.post(TakeoverLoader.ShowEvent.create());
  }

  public static Action0 createHideEventAction(EventBus eventBus) {
    ObjectHelper.checkNotNull(eventBus, "eventBus == null");
    return () -> eventBus.post(TakeoverLoader.HideEvent.create());
  }

  private TakeoverLoaderHelper() {
  }
}
