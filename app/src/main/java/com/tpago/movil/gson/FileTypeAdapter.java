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
final class FileTypeAdapter extends TypeAdapter<File> {

  static FileTypeAdapter create(Gson gson) {
    return new FileTypeAdapter(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private FileTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public File read(JsonReader reader) throws IOException {
    File file = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      file = new File(this.stringTypeAdapter.read(reader));
    }
    return file;
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
