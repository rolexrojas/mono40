package com.tpago.movil.gson;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.user.User;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
final class UserTypeAdapter extends TypeAdapter<User> {

  private static final String PROPERTY_NAME_ID = "id";
  private static final String PROPERTY_NAME_PHONE_NUMBER = "msisdn";
  private static final String PROPERTY_NAME_EMAIL = "email";
  private static final String PROPERTY_NAME_FIRST_NAME = "name";
  private static final String PROPERTY_NAME_LAST_NAME = "last-name";
  private static final String PROPERTY_NAME_PICTURE_URI = "profilePicUrl";

  static UserTypeAdapter create(Gson gson) {
    return new UserTypeAdapter(gson);
  }

  private final TypeAdapter<Email> emailTypeAdapter;
  private final TypeAdapter<Integer> integerTypeAdapter;
  private final TypeAdapter<PhoneNumber> phoneNumberTypeAdapter;
  private final TypeAdapter<String> stringTypeAdapter;
  private final TypeAdapter<Uri> uriTypeAdapter;

  private UserTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

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
          case PROPERTY_NAME_ID:
            builder.id(this.integerTypeAdapter.read(reader));
            break;
          case PROPERTY_NAME_PHONE_NUMBER:
            builder.phoneNumber(this.phoneNumberTypeAdapter.read(reader));
            break;
          case PROPERTY_NAME_EMAIL:
            builder.email(this.emailTypeAdapter.read(reader));
            break;
          case PROPERTY_NAME_FIRST_NAME:
            builder.firstName(this.stringTypeAdapter.read(reader));
            break;
          case PROPERTY_NAME_LAST_NAME:
            builder.lastName(this.stringTypeAdapter.read(reader));
            break;
          case PROPERTY_NAME_PICTURE_URI:
            builder.pictureUri(this.uriTypeAdapter.read(reader));
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
      writer.name(PROPERTY_NAME_ID);
      this.integerTypeAdapter.write(writer, user.id());
      writer.name(PROPERTY_NAME_PHONE_NUMBER);
      this.phoneNumberTypeAdapter.write(writer, user.phoneNumber());
      writer.name(PROPERTY_NAME_EMAIL);
      this.emailTypeAdapter.write(writer, user.email());
      writer.name(PROPERTY_NAME_FIRST_NAME);
      this.stringTypeAdapter.write(writer, user.firstName());
      writer.name(PROPERTY_NAME_LAST_NAME);
      this.stringTypeAdapter.write(writer, user.lastName());
      writer.name(PROPERTY_NAME_PICTURE_URI);
      this.uriTypeAdapter.write(writer, user.pictureUri());
      writer.endObject();
    }
  }
}
