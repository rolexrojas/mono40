package com.mono40.movil.session;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mono40.movil.Email;
import com.mono40.movil.Name;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
public final class UserTypeAdapter extends TypeAdapter<User> {

  private static final String PROPERTY_PHONE_NUMBER = "msisdn";
  private static final String PROPERTY_EMAIL = "email";
  private static final String PROPERTY_FIRST_NAME = "name";
  private static final String PROPERTY_LAST_NAME = "last-name";
  private static final String PROPERTY_ID = "id";
  private static final String PROPERTY_PICTURE = "profilePicUrl";
  private static final String PROPERTY_CARRIER = "carrier";
  private static final String PROPERTY_PASSWORDCHANGE = "passwordChange";

  private static final String PROPERTY_DEFAULT_FIRST_NAME = "Usuario";
  private static final String PROPERTY_DEFAULT_LAST_NAME = "tPago";

  public static UserTypeAdapter create(Gson gson) {
    return new UserTypeAdapter(gson);
  }

  private final TypeAdapter<Partner> carrierTypeAdapter;
  private final TypeAdapter<Email> emailTypeAdapter;
  private final TypeAdapter<Integer> integerTypeAdapter;
  private final TypeAdapter<PhoneNumber> phoneNumberTypeAdapter;
  private final TypeAdapter<String> stringTypeAdapter;
  private final TypeAdapter<Uri> uriTypeAdapter;
  private final TypeAdapter<Boolean> booleanTypeAdapter;

  private UserTypeAdapter(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");

    this.carrierTypeAdapter = gson.getAdapter(Partner.class);
    this.emailTypeAdapter = gson.getAdapter(Email.class);
    this.integerTypeAdapter = gson.getAdapter(Integer.class);
    this.phoneNumberTypeAdapter = gson.getAdapter(PhoneNumber.class);
    this.stringTypeAdapter = gson.getAdapter(String.class);
    this.uriTypeAdapter = gson.getAdapter(Uri.class);
    this.booleanTypeAdapter = gson.getAdapter(Boolean.class);
  }

  @Override
  public User read(JsonReader reader) throws IOException {
    User user = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      final User.Builder builder = User.builder();
      final Name.Builder nameBuilder = Name.builder();
      reader.beginObject();
      while (reader.hasNext()) {
        final String propertyName = reader.nextName();
        switch (propertyName) {
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
            nameBuilder.first(firstName);
            break;
          case PROPERTY_LAST_NAME:
            String lastName = this.stringTypeAdapter.read(reader);
            if (StringHelper.isNullOrEmpty(lastName)) {
              lastName = PROPERTY_DEFAULT_LAST_NAME;
            }
            nameBuilder.last(lastName);
            break;
          case PROPERTY_ID:
            builder.id(this.integerTypeAdapter.read(reader));
            break;
          case PROPERTY_PICTURE:
            builder.picture(this.uriTypeAdapter.read(reader));
            break;
          case PROPERTY_CARRIER:
            builder.carrier(this.carrierTypeAdapter.read(reader));
            break;
          case PROPERTY_PASSWORDCHANGE:
            builder.passwordChange(this.booleanTypeAdapter.read(reader));
            break;
          default:
            reader.skipValue();
            break;
        }
      }
      reader.endObject();
      user = builder
        .name(nameBuilder.build())
        .build();
    }
    return user;
  }

  @Override
  public void write(JsonWriter writer, User user) throws IOException {
    if (ObjectHelper.isNull(user)) {
      writer.nullValue();
    } else {
      writer.beginObject();
      writer.name(PROPERTY_PHONE_NUMBER);
      this.phoneNumberTypeAdapter.write(writer, user.phoneNumber());
      writer.name(PROPERTY_EMAIL);
      this.emailTypeAdapter.write(writer, user.email());
      writer.name(PROPERTY_FIRST_NAME);
      this.stringTypeAdapter.write(writer, user.firstName());
      writer.name(PROPERTY_LAST_NAME);
      this.stringTypeAdapter.write(writer, user.lastName());
      writer.name(PROPERTY_ID);
      this.integerTypeAdapter.write(writer, user.id());
      writer.name(PROPERTY_PICTURE);
      this.uriTypeAdapter.write(writer, user.picture());
      writer.name(PROPERTY_CARRIER);
      this.carrierTypeAdapter.write(writer, user.carrier());
      writer.name(PROPERTY_PASSWORDCHANGE);
      this.booleanTypeAdapter.write(writer, user.passwordChanges());

      writer.endObject();
    }
  }
}
