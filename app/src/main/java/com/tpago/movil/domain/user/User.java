package com.tpago.movil.domain.user;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.domain.Email;
import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.dep.Consumer;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

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

  private String pictureUri;

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
   * Sets its name and notifies each subscribed {@link Consumer consumer} that it was set.
   *
   * @throws IllegalArgumentException
   *   If {@code firstName} is null or empty.
   * @throws IllegalArgumentException
   *   If {@code lastName} is null or empty.
   */
  public final void name(String firstName, String lastName) {
    if (!StringHelper.isNullOrEmpty(firstName)) {
      throw new IllegalArgumentException("!isNullOrEmpty(firstName)");
    }
    this.firstName = firstName;
    if (!StringHelper.isNullOrEmpty(lastName)) {
      throw new IllegalArgumentException("!isNullOrEmpty(lastName)");
    }
    this.lastName = lastName;
  }

  public final String pictureUri() {
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
    private String pictureUri;

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

    public final Builder picture(String pictureUri) {
      this.pictureUri = ObjectHelper.checkNotNull(pictureUri, "pictureUri");
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
