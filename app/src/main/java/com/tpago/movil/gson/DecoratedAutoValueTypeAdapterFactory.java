package com.tpago.movil.gson;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.bank.*;
import com.tpago.movil.company.LogoCatalogMapper;
import com.tpago.movil.currency.Currency;
import com.tpago.movil.currency.CurrencyTypeAdapter;
import com.tpago.movil.partner.Carrier;
import com.tpago.movil.partner.CarrierTypeAdapter;
import com.tpago.movil.partner.Provider;
import com.tpago.movil.partner.ProviderTypeAdapter;
import com.tpago.movil.session.UpdateUserCarrierJob;
import com.tpago.movil.session.UpdateUserCarrierJobTypeAdapter;
import com.tpago.movil.session.UpdateUserNameJob;
import com.tpago.movil.session.UpdateUserNameJobTypeAdapter;
import com.tpago.movil.session.UpdateUserPictureJob;
import com.tpago.movil.session.UpdateUserPictureJobTypeAdapter;
import com.tpago.movil.session.User;
import com.tpago.movil.session.UserTypeAdapter;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;

import java.io.File;

/**
 * @author hecvasro
 */
final class DecoratedAutoValueTypeAdapterFactory implements TypeAdapterFactory {

  static DecoratedAutoValueTypeAdapterFactory create(LogoCatalogMapper logoCatalogMapper) {
    return new DecoratedAutoValueTypeAdapterFactory(logoCatalogMapper);
  }

  private final LogoCatalogMapper logoCatalogMapper;

  private final TypeAdapterFactory typeAdapterFactory;

  private DecoratedAutoValueTypeAdapterFactory(LogoCatalogMapper logoCatalogMapper) {
    this.logoCatalogMapper = ObjectHelper.checkNotNull(logoCatalogMapper, "logoCatalogMapper");

    this.typeAdapterFactory = AutoValueTypeAdapterFactory.create();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    final Class<T> rawType = (Class<T>) type.getRawType();
    if (Uri.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) UriTypeAdapter.create(gson);
    } else if (File.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) FileTypeAdapter.create(gson);
    } else if (PhoneNumber.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) PhoneNumberTypeAdapter.create(gson);
    } else if (Email.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) EmailTypeAdapter.create(gson);
    } else if (Password.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) PasswordTypeAdapter.create(gson);
    } else if (Currency.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) CurrencyTypeAdapter.create(gson);
    } else if (FailureData.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) FailureDataTypeAdapter.create(gson);
    } else if (Bank.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) BankTypeAdapter.create(this.logoCatalogMapper, gson);
    } else if (Carrier.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) CarrierTypeAdapter.create(this.logoCatalogMapper, gson);
    } else if (Provider.class.isAssignableFrom(rawType)) {
      return (TypeAdapter<T>) ProviderTypeAdapter.create(this.logoCatalogMapper, gson);
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
