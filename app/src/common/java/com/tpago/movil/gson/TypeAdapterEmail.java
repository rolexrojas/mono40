package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.Email;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class TypeAdapterEmail extends TypeAdapter<Email> {

  static TypeAdapterEmail create(Gson gson) {
    return new TypeAdapterEmail(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private TypeAdapterEmail(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");
    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public Email read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    return Email.create(this.stringTypeAdapter.read(reader));
  }

  @Override
  public void write(JsonWriter writer, Email email) throws IOException {
    if (ObjectHelper.isNull(email)) {
      writer.nullValue();
    } else {
      this.stringTypeAdapter.write(writer, email.value());
    }
  }
}
