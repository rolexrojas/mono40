package com.tpago.movil.session;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.Email;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class User {

  public static Builder builder() {
    return new AutoValue_User.Builder();
  }

  private String firstName;
  private String lastName;
  private final BehaviorSubject<Pair<String, String>> nameSubject = BehaviorSubject.create();

  private Uri picture;
  private final BehaviorSubject<Uri> pictureSubject = BehaviorSubject.create();

  private Carrier carrier;
  private final BehaviorSubject<Carrier> carrierSubject = BehaviorSubject.create();

  private final BehaviorSubject<User> subject = BehaviorSubject.create();

  User() {
  }

  public abstract int id();

  public abstract PhoneNumber phoneNumber();

  public abstract Email email();

  final Observable<User> changes() {
    return this.subject;
  }

  private void dispatchChanges() {
    this.subject.onNext(this);
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

  public final Observable<Pair<String, String>> nameChanges() {
    return this.nameSubject;
  }

  final void updateName(String firstName, String lastName) {
    this.firstName = StringHelper.checkIsNotNullNorEmpty(firstName, "firstName");
    this.lastName = StringHelper.checkIsNotNullNorEmpty(lastName, "lastName");
    this.nameSubject.onNext(Pair.create(this.firstName, this.lastName));

    this.dispatchChanges();
  }

  @Nullable
  public final Uri picture() {
    return this.picture;
  }

  public final Observable<Uri> pictureChanges() {
    return this.pictureSubject;
  }

  final void updatePicture(Uri picture) {
    this.picture = ObjectHelper.checkNotNull(picture, "picture");
    this.pictureSubject.onNext(this.picture);

    this.dispatchChanges();
  }

  @Nullable
  public final Carrier carrier() {
    return this.carrier;
  }

  public final Observable<Carrier> carrierChanges() {
    return this.carrierSubject;
  }

  final void updateCarrier(Carrier carrier) {
    this.carrier = ObjectHelper.checkNotNull(carrier, "carrier");
    this.carrierSubject.onNext(this.carrier);

    this.dispatchChanges();
  }

  @Override
  public String toString() {
    return new StringBuilder("User")
      .append("{")
      .append("id=")
      .append(this.id())
      .append(",phoneNumber=")
      .append(this.phoneNumber())
      .append(",email=")
      .append(this.email())
      .append(",firstName=")
      .append(this.firstName)
      .append(",lastName=")
      .append(this.lastName)
      .append(",picture=")
      .append(this.picture)
      .append(",carrier=")
      .append(this.carrier)
      .append("}")
      .toString();
  }

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
