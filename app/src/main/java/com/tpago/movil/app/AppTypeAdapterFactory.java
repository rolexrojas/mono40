package com.tpago.movil.app;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * @author hecvasro
 */
@GsonTypeAdapterFactory
public abstract class AppTypeAdapterFactory implements TypeAdapterFactory {
  public static AppTypeAdapterFactory create() {
    return new AutoValueGson_AppTypeAdapterFactory();
  }
}
