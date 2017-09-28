package com.tpago.movil.dep;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.tpago.movil.dep.text.Texts;

/**
 * @author hecvasro
 */
@Deprecated
@AutoValue
public abstract class Session implements Parcelable {
  private static Session create(String token) {
    return new AutoValue_Session(token);
  }

  protected Session() {
  }

  public abstract String getToken();

  public static final class Builder {
    private String token;

    public final boolean canBuild() {
      return Texts.checkIfNotEmpty(token);
    }

    public final Builder setToken(String token) {
      if (Texts.checkIfEmpty(token)) {
        throw new IllegalStateException("Texts.checkIfEmpty(token) == true");
      }
      this.token = token;
      return this;
    }

    public final Session build() {
      if (!canBuild()) {
        throw new IllegalStateException("canBuild() == false");
      }
      return create(token);
    }
  }
}
