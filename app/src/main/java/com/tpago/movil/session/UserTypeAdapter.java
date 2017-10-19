package com.tpago.movil.session;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
public final class UserTypeAdapter extends TypeAdapter<User> {

  private static final String PROPERTY_ID = "id";
  private static final String PROPERTY_PHONE_NUMBER = "msisdn";
  private static final String PROPERTY_EMAIL = "email";
  private static final String PROPERTY_FIRST_NAME = "name";
  private static final String PROPERTY_LAST_NAME = "last-name";
  private static final String PROPERTY_PICTURE = "profilePicUrl";
  private static final String PROPERTY_CARRIER = "carrier";

  private static final String PROPERTY_DEFAULT_FIRST_NAME = "Usuario";
  private static final String PROPERTY_DEFAULT_LAST_NAME = "tPago";

  public static UserTypeAdapter create(Gson gson) {
    return new UserTypeAdapter(gson);
  }

  private final TypeAdapter<Carrier> carrierTypeAdapter;
  private final TypeAdapter<Email> emailTypeAdapter;
  private final TypeAdapter<Integer> integerTypeAdapter;
  private final TypeAdapter<PhoneNumber> phoneNumberTypeAdapter;
  private final TypeAdapter<String> stringTypeAdapter;
  private final TypeAdapter<Uri> uriTypeAdapter;

  private UserTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.carrierTypeAdapter = gson.getAdapter(Carrier.class);
    this.emailTypeAdapter = gson.getAdapter(Email.class);
    this.integerTypeAdapter = gson.getAdapter(Integer.class);
    this.phoneNumberTypeAdapter = gson.getAdapter(PhoneNumber.class);
    this.stringTypeAdapter = gson.getAdapter(String.class);
    this.uriTypeAdapter = gson.getAdapter(Uri.class);
  }

  @Override
  public User read(JsonReader reader) throws IOException {
    User user = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      final User.Builder builder = User.builder();
      reader.beginObject();
      while (reader.hasNext()) {
        final String propertyName = reader.nextName();
        switch (propertyName) {
          case PROPERTY_ID:
            builder.id(this.integerTypeAdapter.read(reader));
            break;
          case PROPERTY_PHONE_NUMBER:
            builder.phoneNumber(this.phoneNumberTypeAdapter.read(reader));
            break;
          case PROPERTY_EMAIL:
            builder.email(this.emailTypeAdapter.read(reader));
            break;
          case PROPERTY_FIRST_NAME:
            String firstName = this.stringTypeAdapter.read(reader);
            if (StringHelper.isNullOrEmpty(firstName)) {
              firstName = PROPERTY_DEFAULT_FIRST_NAME;
            }
            builder.firstName(firstName);
            break;
          case PROPERTY_LAST_NAME:
            String lastName = this.stringTypeAdapter.read(reader);
            if (StringHelper.isNullOrEmpty(lastName)) {
              lastName = PROPERTY_DEFAULT_LAST_NAME;
            }
            builder.lastName(lastName);
            break;
          case PROPERTY_PICTURE:
            builder.picture(this.uriTypeAdapter.read(reader));
            break;
          case PROPERTY_CARRIER:
            builder.carrier(this.carrierTypeAdapter.read(reader));
            break;
          default:
            reader.skipValue();
            break;
        }
      }
      reader.endObject();
      user = builder.build();
    }
    return user;
  }

  @Override
  public void write(JsonWriter writer, User user) throws IOException {
    if (ObjectHelper.isNull(user)) {
      writer.nullValue();
    } else {
      writer.beginObject();
      writer.name(PROPERTY_ID);
      this.integerTypeAdapter.write(writer, user.id());
      writer.name(PROPERTY_PHONE_NUMBER);
      this.phoneNumberTypeAdapter.write(writer, user.phoneNumber());
      writer.name(PROPERTY_EMAIL);
      this.emailTypeAdapter.write(writer, user.email());
      writer.name(PROPERTY_FIRST_NAME);
      this.stringTypeAdapter.write(writer, user.firstName());
      writer.name(PROPERTY_LAST_NAME);
      this.stringTypeAdapter.write(writer, user.lastName());
      writer.name(PROPERTY_PICTURE);
      this.uriTypeAdapter.write(writer, user.picture());
      writer.name(PROPERTY_CARRIER);
      this.carrierTypeAdapter.write(writer, user.carrier());
      writer.endObject();
    }
  }
}
