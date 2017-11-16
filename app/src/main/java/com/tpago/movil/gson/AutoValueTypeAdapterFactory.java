package com.tpago.movil.gson;

import com.google.gson.TypeAdapterFactory;

/**
 * @author hecvasro
 */
@com.ryanharter.auto.value.gson.GsonTypeAdapterFactory
abstract class AutoValueTypeAdapterFactory implements TypeAdapterFactory {

  static AutoValueTypeAdapterFactory create() {
    return new AutoValueGson_AutoValueTypeAdapterFactory();
  }
}
