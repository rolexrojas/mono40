package com.tpago.movil.api;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

/**
 * @author hecvasro
 */
@GsonTypeAdapterFactory
abstract class ApiTypeAdapterFactory implements TypeAdapterFactory {
  static ApiTypeAdapterFactory create() {
    return new AutoValueGson_ApiTypeAdapterFactory();
  }
}
