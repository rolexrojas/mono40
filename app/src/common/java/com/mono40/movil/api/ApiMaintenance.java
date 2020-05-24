package com.mono40.movil.api;

import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mono40.movil.ServiceInformation.CarServiceInformation;
import com.mono40.movil.ServiceInformation.Maintenance;
import com.mono40.movil.session.User;
import com.mono40.movil.util.ObjectHelper;

public class ApiMaintenance {
/*
    public static TypeAdapter<ApiMaintenance> typeAdapter(Gson gson) {
        return new AutoValue_ApiMaintenance.GsonTypeAdapter(gson);
    }

    private static ApiMaintenance.Builder builder() {
        return new AutoValue_ApiMaintenance.Builder();
    }


    private static ApiMaintenance.Builder builder(CarServiceInformation carServiceInformation) {

        final String entityDetail = user.phoneNumber()
                .value();
        final Partner carrier = user.carrier();
        final Integer entityId = ObjectHelper.isNotNull(carrier) ? carrier.code() : null;
        final String name = user.name()
                .toString();
        return builder()
                .entityDetail(entityDetail)
                .entityId(entityId)
                .isEntityPhoneNumber(true)
                .name(name);
    }

    public static ApiMaintenance create(CarServiceInformation user, Maintenance maintenance) {
        //ObjectHelper.checkNotNull(name, "name");
        return builder(user)
                .name(maintenance.toString())
                .build();
    }

    ApiMaintenance() {
    }

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

        public abstract ApiMaintenance.Builder entityDetail(String entityDetail);

        public abstract ApiMaintenance.Builder entityId(@Nullable Integer entityId);

        public abstract ApiMaintenance.Builder isEntityPhoneNumber(boolean isEntityPhoneNumber);

        public abstract ApiMaintenance.Builder name(String name);

        public abstract ApiMaintenance build();
    }
*/
}
