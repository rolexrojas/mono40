package com.tpago.movil.data.gson;

import android.net.Uri;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.dep.api.UserData;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.user.User;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.io.IOException;

/**
 * @author hecvasro
 */
public final class UserTypeAdapter extends TypeAdapter<User> {

  public static UserTypeAdapter create(TypeAdapter<UserData> typeAdapter) {
    return new UserTypeAdapter(typeAdapter);
  }

  private final TypeAdapter<UserData> typeAdapter;

  private UserTypeAdapter(TypeAdapter<UserData> typeAdapter) {
    this.typeAdapter = ObjectHelper.checkNotNull(typeAdapter, "typeAdapter");
  }

  @Override
  public User read(JsonReader reader) throws IOException {
    final UserData userData = this.typeAdapter.read(reader);
    User user = null;
    if (ObjectHelper.isNotNull(userData)) {
      final String uriString = userData.pictureUri();
      Uri pictureUri = null;
      if (!StringHelper.isNullOrEmpty(uriString)) {
        pictureUri = Uri.parse(uriString);
      }
      user = User.builder()
        .id(userData.id())
        .phoneNumber(PhoneNumber.create(userData.phoneNumber()))
        .email(Email.create(userData.email()))
        .firstName(userData.firstName())
        .lastName(userData.lastName())
        .pictureUri(pictureUri)
        .build();
    }
    return user;
  }

  @Override
  public void write(JsonWriter writer, User user) throws IOException {
    UserData userData = null;
    if (ObjectHelper.isNotNull(user)) {
      final Uri pictureUri = user.pictureUri();
      String pictureString = null;
      if (ObjectHelper.isNotNull(pictureUri)) {
        pictureString = pictureUri.getPath();
      }
      userData = UserData.createBuilder()
        .id(user.id())
        .phoneNumber(
          user.phoneNumber()
            .value()
        )
        .email(
          user.email()
            .value()
        )
        .firstName(user.firstName())
        .lastName(user.lastName())
        .pictureUri(pictureString)
        .build();
    }
    this.typeAdapter.write(writer, userData);
  }
}
