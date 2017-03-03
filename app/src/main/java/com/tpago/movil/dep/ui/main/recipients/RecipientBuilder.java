package com.tpago.movil.dep.ui.main.recipients;

import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.util.Objects;

import java.io.Serializable;

import rx.Observable;

/**
 * @author hecvasro
 */
public abstract class RecipientBuilder implements Serializable {
  public abstract String getImagePath();
  public abstract String getTitle();

  public abstract Observable<Result> build(String number, String pin);

  public static final class Result {
    private final Recipient recipient;
    private final String error;

    public Result(Recipient recipient) {
      this.recipient = recipient;
      this.error = null;
    }

    public Result(String error) {
      this.recipient = null;
      this.error = error;
    }

    public final boolean isSuccessful() {
      return Objects.isNotNull(recipient);
    }

    public final Recipient getData() {
      return recipient;
    }

    public final String getError() {
      return error;
    }
  }
}
