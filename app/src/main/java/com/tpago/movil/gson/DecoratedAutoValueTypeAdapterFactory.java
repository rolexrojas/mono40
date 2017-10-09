package com.tpago.movil.gson;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.user.User;
import com.tpago.movil.util.FailureData;

/**
 * @author hecvasro
 */
final class DecoratedAutoValueTypeAdapterFactory implements TypeAdapterFactory {

  static DecoratedAutoValueTypeAdapterFactory create() {
    return new DecoratedAutoValueTypeAdapterFactory();
  }

  private final TypeAdapterFactory typeAdapterFactory;

  private DecoratedAutoValueTypeAdapterFactory() {
    this.typeAdapterFactory = AutoValueTypeAdapterFactory.create();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    final Class<T> rawType = (Class<T>) type.getRawType();
    if (Uri.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) UriTypeAdapter.create(gson);
    } else if (PhoneNumber.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) PhoneNumberTypeAdapter.create(gson);
    } else if (Email.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) EmailTypeAdapter.create(gson);
    } else if (User.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) UserTypeAdapter.create(gson);
    } else if (FailureData.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) FailureDataTypeAdapter.create(gson);
    } else {
      return this.typeAdapterFactory.create(gson, type);
    }
  }
}
