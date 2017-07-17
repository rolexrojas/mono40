package com.tpago.movil.gson;

import com.google.gson.TypeAdapterFactory;

/**
 * @author hecvasro
 */
@com.ryanharter.auto.value.gson.GsonTypeAdapterFactory
abstract class GeneratedTypeAdapterFactory implements TypeAdapterFactory {
  static GeneratedTypeAdapterFactory create() {
    return new AutoValueGson_GeneratedTypeAdapterFactory();
  }
}
