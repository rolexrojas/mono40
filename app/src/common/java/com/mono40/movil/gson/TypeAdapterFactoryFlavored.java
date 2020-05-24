package com.mono40.movil.gson;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.mono40.movil.Currency;
import com.mono40.movil.Email;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.lib.Password;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.ObjectHelper;

import java.io.File;

/**
 * @author hecvasro
 */
final class TypeAdapterFactoryFlavored implements TypeAdapterFactory {

  static Builder builder() {
    return new Builder();
  }

  private final TypeAdapterFactory factory;

  private TypeAdapterFactoryFlavored(Builder builder) {
    this.factory = builder.factory;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    final Class<T> rawType = (Class<T>) type.getRawType();
    if (Uri.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) TypeAdapterUri.create(gson);
    } else if (File.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) TypeAdapterFile.create(gson);
    } else if (PhoneNumber.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) TypeAdapterPhoneNumber.create(gson);
    } else if (Email.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) TypeAdapterEmail.create(gson);
    } else if (Password.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) TypeAdapterPassword.create(gson);
    } else if (Currency.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) TypeAdapterCurrency.create(gson);
    } else if (Bank.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) TypeAdapterBank.create(gson);
    } else {
      return this.factory.create(gson, type);
    }
  }

  static final class Builder {

    private TypeAdapterFactory factory;

    private Builder() {
    }

    final Builder factory(TypeAdapterFactory factory) {
      this.factory = ObjectHelper.checkNotNull(factory, "factory");
      return this;
    }

    final TypeAdapterFactoryFlavored build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("factory", ObjectHelper.isNull(this.factory))
        .checkNoMissingProperties();
      return new TypeAdapterFactoryFlavored(this);
    }
  }
}
