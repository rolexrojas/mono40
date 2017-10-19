package com.tpago.movil.session;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
public final class UpdateUserCarrierJobTypeAdapter extends TypeAdapter<UpdateUserCarrierJob> {

  public static UpdateUserCarrierJobTypeAdapter create(Gson gson) {
    return new UpdateUserCarrierJobTypeAdapter(gson);
  }

  private static final String PROPERTY_CARRIER = "carrier";

  private final TypeAdapter<Carrier> carrierTypeAdapter;

  private UpdateUserCarrierJobTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.carrierTypeAdapter = gson.getAdapter(Carrier.class);
  }

  @Override
  public UpdateUserCarrierJob read(JsonReader reader) throws IOException {
    UpdateUserCarrierJob job = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      Carrier carrier = null;

      reader.beginObject();
      while (reader.hasNext()) {
        final String propertyName = reader.nextName();
        switch (propertyName) {
          case PROPERTY_CARRIER:
            carrier = this.carrierTypeAdapter.read(reader);
            break;
          default:
            reader.skipValue();
            break;
        }
      }
      reader.endObject();

      job = UpdateUserCarrierJob.create(carrier);
    }
    return job;
  }

  @Override
  public void write(JsonWriter writer, UpdateUserCarrierJob job) throws IOException {
    if (ObjectHelper.isNull(job)) {
      writer.nullValue();
    } else {
      writer.beginObject();
      writer.name(PROPERTY_CARRIER);
      this.carrierTypeAdapter.write(writer, job.carrier);
      writer.endObject();
    }
  }
}
