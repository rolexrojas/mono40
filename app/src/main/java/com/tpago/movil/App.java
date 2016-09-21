package com.tpago.movil;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class App extends Application {
  /**
   * TODO
   */
  private AppComponent component;

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final AppComponent getComponent() {
    if (component == null) {
      component = DaggerAppComponent.builder()
        .appModule(new AppModule(this))
        .build();
    }
    return component;
  }
}
