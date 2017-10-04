package com.tpago.movil.dep.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.tpago.movil.data.gson.UserTypeAdapter;
import com.tpago.movil.dep.api.UserData;
import com.tpago.movil.user.User;

final class DecoratedGeneratedTypeAdapterFactory implements TypeAdapterFactory {

  static DecoratedGeneratedTypeAdapterFactory create() {
    return new DecoratedGeneratedTypeAdapterFactory();
  }

  private final TypeAdapterFactory typeAdapterFactory;

  private DecoratedGeneratedTypeAdapterFactory() {
    this.typeAdapterFactory = GeneratedTypeAdapterFactory.create();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    Class<T> rawType = (Class<T>) type.getRawType();
    if (User.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) UserTypeAdapter.create(UserData.typeAdapter(gson));
    } else {
      return this.typeAdapterFactory.create(gson, type);
    }
  }
}
