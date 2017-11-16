package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.util.FailureData;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class FailureDataTypeAdapter extends TypeAdapter<FailureData> {

  static FailureDataTypeAdapter create(Gson gson) {
    return new FailureDataTypeAdapter(gson);
  }

  private static final String PROPERTY_NAME_ERROR = "error";
  private static final String PROPERTY_NAME_CODE = "code";
  private static final String PROPERTY_NAME_DESCRIPTION = "description";

  private final TypeAdapter<Integer> integerTypeAdapter;
  private final TypeAdapter<String> stringTypeAdapter;

  private FailureDataTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.integerTypeAdapter = gson.getAdapter(Integer.class);
    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public FailureData read(JsonReader reader) throws IOException {
    FailureData failureData = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      reader.beginObject();
      reader.nextName();
      reader.beginObject();
      final FailureData.Builder builder = FailureData.builder();
      while (reader.hasNext()) {
        final String propertyName = reader.nextName();
        switch (propertyName) {
          case PROPERTY_NAME_CODE:
            builder.code(this.integerTypeAdapter.read(reader));
            break;
          case PROPERTY_NAME_DESCRIPTION:
            builder.description(this.stringTypeAdapter.read(reader));
            break;
          default:
            reader.skipValue();
            break;
        }
      }
      failureData = builder.build();
      reader.endObject();
      reader.endObject();
    }
    return failureData;
  }

  @Override
  public void write(JsonWriter writer, FailureData failureData) throws IOException {
    if (ObjectHelper.isNull(failureData)) {
      writer.nullValue();
    } else {
      writer.beginObject();
      writer.name(PROPERTY_NAME_ERROR);
      writer.beginObject();
      writer.name(PROPERTY_NAME_CODE);
      this.integerTypeAdapter.write(writer, failureData.code());
      writer.name(PROPERTY_NAME_DESCRIPTION);
      this.stringTypeAdapter.write(writer, failureData.description());
      writer.endObject();
      writer.endObject();
    }
  }
}
