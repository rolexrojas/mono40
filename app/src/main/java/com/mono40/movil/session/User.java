package com.mono40.movil.session;

import android.net.Uri;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.mono40.movil.Email;
import com.mono40.movil.Name;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.util.BuilderChecker;
import com.mono40.movil.util.ObjectHelper;

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

  private Name name;
  private final BehaviorSubject<Name> nameSubject = BehaviorSubject.create();

  private Integer id;
  private final BehaviorSubject<Integer> idSubject = BehaviorSubject.create();

  private Uri picture;
  private final BehaviorSubject<Uri> pictureSubject = BehaviorSubject.create();

  private boolean passwordChange;

  private Partner carrier;
  private final BehaviorSubject<Partner> carrierSubject = BehaviorSubject.create();

  private final BehaviorSubject<User> subject = BehaviorSubject.create();

  User() {
  }

  public abstract PhoneNumber phoneNumber();

  public abstract Email email();

  final Observable<User> changes() {
    return this.subject;
  }

  private void dispatchChanges() {
    this.subject.onNext(this);
  }

  public final String firstName() {
    return this.name.first();
  }

  public final String lastName() {
    return this.name.last();
  }

  public final Name name() {
    return this.name;
  }

  public final Observable<Name> nameChanges() {
    return this.nameSubject;
  }

  final void updateName(Name name) {
    this.name = ObjectHelper.checkNotNull(name, "name");
    this.nameSubject.onNext(this.name);
    this.dispatchChanges();
  }

  @Nullable
  public final Integer id() {
    return this.id;
  }

  public final Observable<Integer> idObservable() {
    return this.idSubject;
  }

  final void updateId(Integer id) {
    this.id = ObjectHelper.checkNotNull(id, "id");
    this.dispatchChanges();
  }

  @Nullable
  public final Uri picture() {
    return this.picture;
  }

  public final Observable<Uri> pictureChanges() {
    return this.pictureSubject;
  }

  public final void updatePicture(Uri picture) {
    this.picture = picture;
    this.pictureSubject.onNext(this.picture);

    this.dispatchChanges();
  }

  public final boolean passwordChanges(){
    return this.passwordChange;
  }

  @Nullable
  public final Partner carrier() {
    return this.carrier;
  }

  public final Observable<Partner> carrierChanges() {
    return this.carrierSubject;
  }

  final void updateCarrier(Partner carrier) {
    this.carrier = ObjectHelper.checkNotNull(carrier, "carrier");
    this.carrierSubject.onNext(this.carrier);

    this.dispatchChanges();
  }

  @Override
  public String toString() {
    return new StringBuilder("User")
      .append("{")
      .append("phoneNumber=")
      .append(this.phoneNumber())
      .append(",email=")
      .append(this.email())
      .append(",name=")
      .append(this.name)
      .append(",id=")
      .append(this.id())
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

    private PhoneNumber phoneNumber;
    private Email email;
    private Name name;
    private Integer id;
    private Uri picture;
    private Partner carrier;
    private boolean passwordChange;

    private Builder() {
    }

    public final Builder phoneNumber(PhoneNumber phoneNumber) {
      this.phoneNumber = ObjectHelper.checkNotNull(phoneNumber, "phoneNumber");
      return this;
    }

    public final Builder email(Email email) {
      this.email = ObjectHelper.checkNotNull(email, "email");
      return this;
    }

    public final Builder name(Name name) {
      this.name = ObjectHelper.checkNotNull(name, "name");
      return this;
    }

    public final Builder passwordChange(boolean passwordChange){
      this.passwordChange = passwordChange;
      return this;
    }

    public final Builder id(@Nullable Integer id) {
      this.id = ObjectHelper.checkNotNull(id, "id");
      return this;
    }

    public final Builder picture(@Nullable Uri picture) {
      this.picture = picture;
      return this;
    }

    public final Builder carrier(@Nullable Partner carrier) {
      this.carrier = carrier;
      return this;
    }

    public final User build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("phoneNumber", ObjectHelper.isNull(this.phoneNumber))
        .addPropertyNameIfMissing("email", ObjectHelper.isNull(this.email))
        .addPropertyNameIfMissing("name", ObjectHelper.isNull(this.name))
        .checkNoMissingProperties();
      final User user = new AutoValue_User(this.phoneNumber, this.email);
      user.name = this.name;
      user.id = this.id;
      user.picture = this.picture;
      user.carrier = this.carrier;
      user.passwordChange = this.passwordChange;
      return user;
    }
  }
}
