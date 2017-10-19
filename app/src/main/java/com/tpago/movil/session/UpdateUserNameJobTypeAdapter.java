package com.tpago.movil.session;

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
public final class UpdateUserNameJobTypeAdapter extends TypeAdapter<UpdateUserNameJob> {

  public static UpdateUserNameJobTypeAdapter create(Gson gson) {
    return new UpdateUserNameJobTypeAdapter(gson);
  }

  private static final String PROPERTY_FIRST_NAME = "firstName";
  private static final String PROPERTY_LAST_NAME = "lastName";

  private final TypeAdapter<String> stringTypeAdapter;

  private UpdateUserNameJobTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public UpdateUserNameJob read(JsonReader reader) throws IOException {
    UpdateUserNameJob job = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      String firstName = null;
      String lastName = null;

      reader.beginObject();
      while (reader.hasNext()) {
        final String propertyName = reader.nextName();
        switch (propertyName) {
          case PROPERTY_FIRST_NAME:
            firstName = this.stringTypeAdapter.read(reader);
            break;
          case PROPERTY_LAST_NAME:
            lastName = this.stringTypeAdapter.read(reader);
            break;
          default:
            reader.skipValue();
            break;
        }
      }
      reader.endObject();

      job = UpdateUserNameJob.create(firstName, lastName);
    }
    return job;
  }

  @Override
  public void write(JsonWriter writer, UpdateUserNameJob job) throws IOException {
    if (ObjectHelper.isNull(job)) {
      writer.nullValue();
    } else {
      writer.beginObject();
      writer.name(PROPERTY_FIRST_NAME);
      this.stringTypeAdapter.write(writer, job.firstName);
      writer.name(PROPERTY_LAST_NAME);
      this.stringTypeAdapter.write(writer, job.lastName);
      writer.endObject();
    }
  }
}
