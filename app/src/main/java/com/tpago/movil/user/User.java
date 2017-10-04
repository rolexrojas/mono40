package com.tpago.movil.user;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
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

  private String firstName;
  private String lastName;

  private List<NameConsumer> nameConsumerList = new ArrayList<>();

  private Uri pictureUri;

  User() {
  }

  public abstract int id();

  public abstract PhoneNumber phoneNumber();

  public abstract Email email();

  public final String firstName() {
    return this.firstName;
  }

  public final String lastName() {
    return this.lastName;
  }

  public final String name() {
    return this.firstName + " " + this.lastName;
  }

  /**
   * Sets its updateName.
   *
   * @throws IllegalArgumentException
   *   If {@code firstName} is null or empty.
   * @throws IllegalArgumentException
   *   If {@code lastName} is null or empty.
   */
  public final void updateName(String firstName, String lastName) {
    if (!StringHelper.isNullOrEmpty(firstName)) {
      throw new IllegalArgumentException("!isNullOrEmpty(firstName)");
    }
    if (!StringHelper.isNullOrEmpty(lastName)) {
      throw new IllegalArgumentException("!isNullOrEmpty(lastName)");
    }

    this.firstName = firstName;
    this.lastName = lastName;

    for (NameConsumer consumer : this.nameConsumerList) {
      consumer.accept(this.firstName, this.lastName);
    }
  }

  public final void addNameConsumer(NameConsumer consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (!this.nameConsumerList.contains(consumer)) {
      this.nameConsumerList.add(consumer);
    }
  }

  public final void removeNameConsumer(NameConsumer consumer) {
    ObjectHelper.checkNotNull(consumer, "consumer");
    if (this.nameConsumerList.contains(consumer)) {
      this.nameConsumerList.remove(consumer);
    }
  }

  @Nullable
  public final Uri pictureUri() {
    return this.pictureUri;
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

    private Uri pictureUri;

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

    public final Builder pictureUri(@Nullable Uri pictureUri) {
      this.pictureUri = pictureUri;
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
      user.pictureUri = this.pictureUri;
      return user;
    }
  }
}
