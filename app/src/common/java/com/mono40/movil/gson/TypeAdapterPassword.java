package com.mono40.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mono40.movil.lib.Password;
import com.mono40.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class TypeAdapterPassword extends TypeAdapter<Password> {

  static TypeAdapterPassword create(Gson gson) {
    return new TypeAdapterPassword(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private TypeAdapterPassword(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");
    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public Password read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    return Password.create(this.stringTypeAdapter.read(reader));
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
