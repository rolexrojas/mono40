package com.gbh.movil.data.api;

import com.gbh.movil.domain.Account;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * TODO
 * @author hecvasro
 */
class AccountTypeAdapterFactory implements TypeAdapterFactory {
  private static final String PROPERTY_TYPE = "accountType";
  private static final String PROPERTY_ALIAS = "accountAlias";
  private static final String PROPERTY_NUMBER = "accountNumber";
  private static final String PROPERTY_BANK = "bank";
  private static final String PROPERTY_CURRENCY = "currency";
  private static final String PROPERTY_QUERY_FEE = "queryFee";

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (!Account.class.isAssignableFrom(type.getRawType())) {
      return null;
    } else {
      return new TypeAdapter<T>() {
        @Override
        public void write(JsonWriter out, T value) throws IOException {
        }

        @Override
        public T read(JsonReader in) throws IOException {
          return null;
        }
      };
    }
  }
}
