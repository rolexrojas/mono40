package com.mono40.movil.api;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.mono40.movil.Code;
import com.mono40.movil.Email;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.data.api.DeviceInformationBody;
import com.mono40.movil.lib.Password;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class RetrofitApiSignUpBody {

    public static TypeAdapter<RetrofitApiSignUpBody> typeAdapter(Gson gson) {
        return new AutoValue_RetrofitApiSignUpBody.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_RetrofitApiSignUpBody.Builder();
    }

    RetrofitApiSignUpBody() {
    }

    @SerializedName("msisdn")
    abstract String phoneNumber();

    @SerializedName("email")
    abstract String email();

    @SerializedName("username")
    abstract String username();

    @SerializedName("name")
    abstract String firstName();

    @SerializedName("last-name")
    abstract String lastName();

    @SerializedName("password")
    abstract String password();

    @SerializedName("pin")
    abstract String pin();

    @SerializedName("device-information")
    abstract DeviceInformationBody deviceInformationBody();


    @Memoized
    @Override
    public abstract String toString();

    @Memoized
    @Override
    public abstract int hashCode();

    @AutoValue.Builder
    public static abstract class Builder {

        Builder() {
        }

        abstract Builder phoneNumber(String phoneNumber);

        public final Builder phoneNumber(PhoneNumber phoneNumber) {
            ObjectHelper.checkNotNull(phoneNumber, "phoneNumber");

            return this.phoneNumber(phoneNumber.value());
        }

        abstract Builder email(String email);

        abstract Builder username(String username);

        public final Builder email(Email email) {
            ObjectHelper.checkNotNull(email, "email");

            final String value = email.value();
            return this.email(value)
                    .username(value);
        }

        public abstract Builder firstName(String firstName);

        public abstract Builder lastName(String lastName);

        abstract Builder password(String password);

        public final Builder password(Password password) {
            ObjectHelper.checkNotNull(password, "password");

            return this.password(password.value());
        }

        abstract Builder pin(String pin);

        public final Builder pin(Code pin) {
            ObjectHelper.checkNotNull(pin, "pin");

            return this.pin(pin.value());
        }

        public abstract Builder deviceInformationBody(DeviceInformationBody deviceInformationBody);

        public abstract RetrofitApiSignUpBody build();
    }
}
