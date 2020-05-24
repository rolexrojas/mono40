package com.mono40.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class TypeAdapterPhoneNumber extends TypeAdapter<PhoneNumber> {

  static TypeAdapterPhoneNumber create(Gson gson) {
    return new TypeAdapterPhoneNumber(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private TypeAdapterPhoneNumber(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");
    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public PhoneNumber read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    return PhoneNumber.create(this.stringTypeAdapter.read(reader));
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
