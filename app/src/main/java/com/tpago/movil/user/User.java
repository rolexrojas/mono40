package com.tpago.movil.user;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.function.Consumer;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * User representation
 *
 * @author hecvasro
 */
@AutoValue
public abstract class User {

  public static Builder builder() {
    return new AutoValue_User.Builder();
  }

  private final List<Consumer<User>> changeConsumers = new ArrayList<>();

  private String firstName;
  private String lastName;
  private final List<NameConsumer> nameConsumers = new ArrayList<>();

  private Uri picture;
  private final List<Consumer<Uri>> pictureConsumers = new ArrayList<>();

  private Carrier carrier;
  private final List<Consumer<Carrier>> carrierConsumers = new ArrayList<>();

  User() {
  }

  public abstract int id();

  public abstract PhoneNumber phoneNumber();

  public abstract Email email();

  final void addChangeConsumer(Consumer<User> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (!this.changeConsumers.contains(consumer)) {
      this.changeConsumers.add(consumer);
    }
  }

  final void removeChangeConsumer(Consumer<User> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (this.changeConsumers.contains(consumer)) {
      this.changeConsumers.remove(consumer);
    }
  }

  private void notifyChangeConsumers() {
    for (Consumer<User> consumer : this.changeConsumers) {
      consumer.accept(this);
    }
  }

  public final String firstName() {
    return this.firstName;
  }

  public final String lastName() {
    return this.lastName;
  }

  public final String name() {
    return this.firstName + " " + this.lastName;
  }

  public final void addNameConsumer(NameConsumer consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (!this.nameConsumers.contains(consumer)) {
      this.nameConsumers.add(consumer);
    }
  }

  public final void removeNameConsumer(NameConsumer consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (this.nameConsumers.contains(consumer)) {
      this.nameConsumers.remove(consumer);
    }
  }

  final void updateName(String firstName, String lastName) {
    if (!StringHelper.isNullOrEmpty(firstName)) {
      throw new IllegalArgumentException("!isNullOrEmpty(firstName)");
    }
    if (!StringHelper.isNullOrEmpty(lastName)) {
      throw new IllegalArgumentException("!isNullOrEmpty(lastName)");
    }
    this.firstName = firstName;
    this.lastName = lastName;
    for (NameConsumer consumer : this.nameConsumers) {
      consumer.accept(this.firstName, this.lastName);
    }
    this.notifyChangeConsumers();
  }

  @Nullable
  public final Uri picture() {
    return this.picture;
  }

  public final void addPictureConsumer(Consumer<Uri> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (!this.pictureConsumers.contains(consumer)) {
      this.pictureConsumers.add(consumer);
    }
  }

  public final void removePictureConsumer(Consumer<Uri> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (this.pictureConsumers.contains(consumer)) {
      this.pictureConsumers.remove(consumer);
    }
  }

  final void updatePicture(Uri picture) {
    this.picture = ObjectHelper.checkNotNull(picture, "picture");
    for (Consumer<Uri> consumer : this.pictureConsumers) {
      consumer.accept(picture);
    }
    this.notifyChangeConsumers();
  }

  @Nullable
  public final Carrier carrier() {
    return this.carrier;
  }

  public final void addCarrierConsumer(Consumer<Carrier> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (!this.carrierConsumers.contains(consumer)) {
      this.carrierConsumers.add(consumer);
    }
  }

  public final void removeCarrierConsumer(Consumer<Carrier> consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (this.carrierConsumers.contains(consumer)) {
      this.carrierConsumers.remove(consumer);
    }
  }

  final void updateCarrier(Carrier carrier) {
    this.carrier = ObjectHelper.checkNotNull(carrier, "carrier");
    for (Consumer<Carrier> consumer : this.carrierConsumers) {
      consumer.accept(this.carrier);
    }
    this.notifyChangeConsumers();
  }

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  public static final class Builder {

    private Integer id;

    private PhoneNumber phoneNumber;
    private Email email;

    private String firstName;
    private String lastName;

    private Uri picture;

    private Carrier carrier;

    private Builder() {
    }

    public final Builder id(Integer id) {
      this.id = ObjectHelper.checkNotNull(id, "id");
      return this;
    }

    public final Builder phoneNumber(PhoneNumber phoneNumber) {
      this.phoneNumber = ObjectHelper.checkNotNull(phoneNumber, "phoneNumber");
      return this;
    }

    public final Builder email(Email email) {
      this.email = ObjectHelper.checkNotNull(email, "email");
      return this;
    }

    public final Builder firstName(String firstName) {
      this.firstName = ObjectHelper.checkNotNull(firstName, "firstName");
      return this;
    }

    public final Builder lastName(String lastName) {
      this.lastName = ObjectHelper.checkNotNull(lastName, "lastName");
      return this;
    }

    public final Builder picture(@Nullable Uri picture) {
      this.picture = picture;
      return this;
    }

    public final Builder carrier(@Nullable Carrier carrier) {
      this.carrier = carrier;
      return this;
    }

    public final User build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("id", ObjectHelper.isNull(this.id))
        .addPropertyNameIfMissing("phoneNumber", ObjectHelper.isNull(this.phoneNumber))
        .addPropertyNameIfMissing("email", ObjectHelper.isNull(this.email))
        .addPropertyNameIfMissing("firstName", StringHelper.isNullOrEmpty(this.firstName))
        .addPropertyNameIfMissing("lastName", StringHelper.isNullOrEmpty(this.lastName))
        .checkNoMissingProperties();

      final User user = new AutoValue_User(this.id, this.phoneNumber, this.email);
      user.firstName = this.firstName;
      user.lastName = this.lastName;
      user.picture = this.picture;
      user.carrier = this.carrier;
      return user;
    }
  }
}
