package com.mono40.movil.data.api;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class DeviceInformationBody {
    public static TypeAdapter<DeviceInformationBody> typeAdapter(Gson gson) {
        return new AutoValue_DeviceInformationBody.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_DeviceInformationBody.Builder();
    }

    DeviceInformationBody() {
    }


    @SerializedName("imei")
    abstract String deviceId();

    @SerializedName("manufacturer")
    abstract String manufacturer();

    @SerializedName("serial")
    abstract String serial();

    @SerializedName("fingerprint")
    abstract String fingerprint();

    @SerializedName("device")
    abstract String device();

    @SerializedName("display")
    abstract String display();

    @SerializedName("board")
    abstract String board();

    @SerializedName("bootloader")
    abstract String bootloader();

    @SerializedName("brand")
    abstract String brand();

    @SerializedName("hardware")
    abstract String hardware();

    @SerializedName("model")
    abstract String model();

    @AutoValue.Builder
    public static abstract class Builder {

        Builder() {
        }

        public abstract Builder deviceId(String deviceId);

        public abstract Builder manufacturer(String manufacturer);

        public abstract Builder serial(String serial);

        public abstract Builder fingerprint(String fingerprint);

        public abstract Builder device(String device);

        public abstract Builder display(String display);

        public abstract Builder board(String board);

        public abstract Builder bootloader(String bootloader);

        public abstract Builder brand(String brand);

        public abstract Builder hardware(String hardware);

        public abstract Builder model(String model);

        public abstract DeviceInformationBody build();
    }
}
