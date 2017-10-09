package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class PhoneNumberTypeAdapter extends TypeAdapter<PhoneNumber> {

  static PhoneNumberTypeAdapter create(Gson gson) {
    return new PhoneNumberTypeAdapter(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private PhoneNumberTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public PhoneNumber read(JsonReader reader) throws IOException {
    PhoneNumber phoneNumber = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      phoneNumber = PhoneNumber.create(this.stringTypeAdapter.read(reader));
    }
    return phoneNumber;
  }

  @Override
  public void write(JsonWriter writer, PhoneNumber phoneNumber) throws IOException {
    if (ObjectHelper.isNull(phoneNumber)) {
      writer.nullValue();
    } else {
      this.stringTypeAdapter.write(writer, phoneNumber.value());
    }
  }
}
