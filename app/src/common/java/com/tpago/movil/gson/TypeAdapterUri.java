package com.tpago.movil.gson;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class TypeAdapterUri extends TypeAdapter<Uri> {

  static TypeAdapterUri create(Gson gson) {
    return new TypeAdapterUri(gson);
  }

  private final TypeAdapter<String> stringTypeAdapter;

  private TypeAdapterUri(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public Uri read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }
    return Uri.parse(this.stringTypeAdapter.read(reader));
  }

  @Override
  public void write(JsonWriter writer, Uri uri) throws IOException {
    if (ObjectHelper.isNull(uri) || uri.equals(Uri.EMPTY)) {
      writer.nullValue();
    } else {
      this.stringTypeAdapter.write(writer, uri.toString());
    }
  }
}
