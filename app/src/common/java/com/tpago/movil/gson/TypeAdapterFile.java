package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.util.ObjectHelper;

import java.io.File;
import java.io.IOException;

/**
 * @author hecvasro
 */
final class TypeAdapterFile extends TypeAdapter<File> {

  static TypeAdapterFile create(Gson gson) {
    return new TypeAdapterFile(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private TypeAdapterFile(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");
    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public File read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    return new File(this.stringTypeAdapter.read(reader));
  }

  @Override
  public void write(JsonWriter writer, File file) throws IOException {
    if (ObjectHelper.isNull(file)) {
      writer.nullValue();
    } else {
      this.stringTypeAdapter.write(writer, file.getAbsolutePath());
    }
  }
}
