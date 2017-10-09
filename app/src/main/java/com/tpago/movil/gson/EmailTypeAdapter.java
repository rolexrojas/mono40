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
final class EmailTypeAdapter extends TypeAdapter<Email> {

  static EmailTypeAdapter create(Gson gson) {
    return new EmailTypeAdapter(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private EmailTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public Email read(JsonReader reader) throws IOException {
    Email email = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      email = Email.create(this.stringTypeAdapter.read(reader));
    }
    return email;
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
