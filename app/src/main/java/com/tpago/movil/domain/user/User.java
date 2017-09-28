package com.tpago.movil.domain.user;

import android.net.Uri;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.common.base.Optional;
import com.tpago.movil.domain.Email;
import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.dep.Consumer;

import java.util.ArrayList;
import java.util.List;

import static com.tpago.movil.util.ObjectHelper.isNull;

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
  private final List<NameConsumer> nameConsumers = new ArrayList<>();

  private Optional<Uri> picture;
  private final List<Consumer<Optional<Uri>>> pictureConsumers = new ArrayList<>();

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
//    checkArgument(!isNullOrEmpty(firstName), "isNullOrEmpty(firstName)");
//    checkArgument(!isNullOrEmpty(lastName), "isNullOrEmpty(lastName)");

    this.firstName = firstName;
    this.lastName = lastName;

    for (NameConsumer consumer : this.nameConsumers) {
      consumer.accept(this.firstName, this.lastName);
    }
  }

  /**
   * Adds the given {@link NameConsumer consumer} to the {@link #nameConsumers list}.
   *
   * @param consumer
   *   {@link NameConsumer Consumer} that will be added.
   *
   * @throws NullPointerException
   *   If {@code consumer} is null.
   */
  public final void addNameConsumer(NameConsumer consumer) {
//    checkNotNull(consumer, "isNull(consumer)");
    if (!this.nameConsumers.contains(consumer)) {
      this.nameConsumers.add(consumer);
    }
  }

  /**
   * Removes the given {@link NameConsumer consumer} from the {@link #nameConsumers list}.
   *
   * @param consumer
   *   {@link NameConsumer Consumer} that will be removed.
   *
   * @throws NullPointerException
   *   If {@code consumer} is null.
   */
  public final void removeNameConsumer(NameConsumer consumer) {
//    checkNotNull(consumer, "isNull(consumer)");
    if (this.nameConsumers.contains(consumer)) {
      this.nameConsumers.add(consumer);
    }
  }

  public final Optional<Uri> picture() {
    return this.picture;
  }

  /**
   * Sets its picture and notifies each subscribed {@link Consumer consumer} that it was set.
   *
   * @throws NullPointerException
   *   If {@code picture} is null.
   */
  public final void picture(Optional<Uri> picture) {
//    checkNotNull(picture, "isNull(picture)");

    this.picture = picture;

    for (Consumer<Optional<Uri>> consumer : this.pictureConsumers) {
      consumer.accept(this.picture);
    }
  }

  /**
   * Adds the given {@link Consumer consumer} to the {@link #pictureConsumers list}.
   *
   * @param consumer
   *   {@link Consumer} that will be added.
   *
   * @throws NullPointerException
   *   If {@code consumer} is null.
   */
  public final void addPictureConsumer(Consumer<Optional<Uri>> consumer) {
//    checkNotNull(consumer, "isNull(consumer)");
    if (!this.pictureConsumers.contains(consumer)) {
      this.pictureConsumers.add(consumer);
    }
  }

  /**
   * Removes the given {@link Consumer consumer} from the {@link #pictureConsumers list}.
   *
   * @param consumer
   *   {@link Consumer} that will be removed.
   *
   * @throws NullPointerException
   *   If {@code consumer} is null.
   */
  public final void removePictureConsumer(Consumer<Optional<Uri>> consumer) {
//    checkNotNull(consumer, "isNull(consumer)");
    if (this.pictureConsumers.contains(consumer)) {
      this.pictureConsumers.remove(consumer);
    }
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
    private Optional<Uri> picture = Optional.absent();

    private Builder() {
    }

    public final Builder id(Integer id) {
//      this.id = checkNotNull(id, "isNull(id)");
      return this;
    }

    public final Builder phoneNumber(PhoneNumber phoneNumber) {
//      this.phoneNumber = checkNotNull(phoneNumber, "isNull(phoneNumber)");
      return this;
    }

    public final Builder email(Email email) {
//      this.email = checkNotNull(email, "isNull(email)");
      return this;
    }

    public final Builder firstName(String firstName) {
//      checkArgument(!isNullOrEmpty(firstName), "isNullOrEmpty(firstName)");
      this.firstName = firstName;
      return this;
    }

    public final Builder lastName(String lastName) {
//      checkArgument(!isNullOrEmpty(lastName), "isNullOrEmpty(lastName)");
      this.lastName = lastName;
      return this;
    }

    public final Builder picture(Optional<Uri> picture) {
//      this.picture = checkNotNull(picture, "isNull(picture)");
      return this;
    }

    public final User build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("id", isNull(this.id))
        .addPropertyNameIfMissing("phoneNumber", isNull(this.phoneNumber))
        .addPropertyNameIfMissing("email", isNull(this.email))
//        .addPropertyNameIfMissing("firstName", isNullOrEmpty(this.firstName))
//        .addPropertyNameIfMissing("lastName", isNullOrEmpty(this.lastName))
        .checkNoMissingProperties();
      throw new UnsupportedOperationException("not implemented");
    }
  }
}
