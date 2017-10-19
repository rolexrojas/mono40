package com.tpago.movil.session;

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
public final class UpdateUserPictureJobTypeAdapter extends TypeAdapter<UpdateUserPictureJob> {

  public static UpdateUserPictureJobTypeAdapter create(Gson gson) {
    return new UpdateUserPictureJobTypeAdapter(gson);
  }

  private static final String PROPERTY_PICTURE = "picture";

  private final TypeAdapter<File> fileTypeAdapter;

  private UpdateUserPictureJobTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.fileTypeAdapter = gson.getAdapter(File.class);
  }

  @Override
  public UpdateUserPictureJob read(JsonReader reader) throws IOException {
    UpdateUserPictureJob job = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      File picture = null;

      reader.beginObject();
      while (reader.hasNext()) {
        final String propertyName = reader.nextName();
        switch (propertyName) {
          case PROPERTY_PICTURE:
            picture = this.fileTypeAdapter.read(reader);
            break;
          default:
            reader.skipValue();
            break;
        }
      }
      reader.endObject();

      job = UpdateUserPictureJob.create(picture);
    }
    return job;
  }

  @Override
  public void write(JsonWriter writer, UpdateUserPictureJob job) throws IOException {
    if (ObjectHelper.isNull(job)) {
      writer.nullValue();
    } else {
      writer.beginObject();
      writer.name(PROPERTY_PICTURE);
      this.fileTypeAdapter.write(writer, job.picture);
      writer.endObject();
    }
  }
}
