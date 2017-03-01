package com.tpago.movil;

import com.google.auto.value.AutoValue;
import com.tpago.movil.text.Texts;

/**
 * @author hecvasro
 */
@AutoValue
public abstract class Session {
  private static Session create(String token) {
    return new AutoValue_Session(token);
  }

  protected Session() {
  }

  public abstract String getToken();

  public static final class Builder {
    private String token;

    private static String checkNotEmpty(String token) {
      if (Texts.isEmpty(token)) {
        throw new IllegalStateException("Texts.isEmpty(token) == true");
      }
      return token;
    }

    public final boolean canBuild() {
      return Texts.isNotEmpty(token);
    }

    public final Builder setToken(String token) {
      this.token = checkNotEmpty(token);
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
