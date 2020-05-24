package com.mono40.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.mono40.movil.session.UpdateUserCarrierJob;
import com.mono40.movil.session.UpdateUserCarrierJobTypeAdapter;
import com.mono40.movil.session.UpdateUserNameJob;
import com.mono40.movil.session.UpdateUserNameJobTypeAdapter;
import com.mono40.movil.session.UpdateUserPictureJob;
import com.mono40.movil.session.UpdateUserPictureJobTypeAdapter;
import com.mono40.movil.session.User;
import com.mono40.movil.session.UserTypeAdapter;
import com.mono40.movil.util.FailureData;

/**
 * @author hecvasro
 */
final class TypeAdapterFactoryDeprecated implements TypeAdapterFactory {

  static TypeAdapterFactoryDeprecated create() {
    return new TypeAdapterFactoryDeprecated();
  }

  private final TypeAdapterFactory typeAdapterFactory;

  private TypeAdapterFactoryDeprecated() {
    this.typeAdapterFactory = TypeAdapterFactoryGenerated.create();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    final Class<T> rawType = (Class<T>) type.getRawType();
    if (FailureData.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) TypeAdapterFailureData.create(gson);
    } else if (User.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) UserTypeAdapter.create(gson);
    } else if (UpdateUserNameJob.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) UpdateUserNameJobTypeAdapter.create(gson);
    } else if (UpdateUserPictureJob.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) UpdateUserPictureJobTypeAdapter.create(gson);
    } else if (UpdateUserCarrierJob.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) UpdateUserCarrierJobTypeAdapter.create(gson);
    } else {
      return this.typeAdapterFactory.create(gson, type);
    }
  }
}
