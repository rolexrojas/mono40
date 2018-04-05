package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.lib.Password;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class PasswordTypeAdapter extends TypeAdapter<Password> {

  static PasswordTypeAdapter create(Gson gson) {
    return new PasswordTypeAdapter(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private PasswordTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public Password read(JsonReader reader) throws IOException {
    Password password = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      password = Password.create(this.stringTypeAdapter.read(reader));
    }
    return password;
  }

  @Override
  public void write(JsonWriter writer, Password password) throws IOException {
    if (ObjectHelper.isNull(password)) {
      writer.nullValue();
    } else {
      this.stringTypeAdapter.write(writer, password.value());
    }
  }
}
