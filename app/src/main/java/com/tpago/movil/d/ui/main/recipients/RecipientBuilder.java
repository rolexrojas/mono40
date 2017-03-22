package com.tpago.movil.d.ui.main.recipients;

import android.content.Context;
import android.net.Uri;

import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.util.Objects;

import rx.Observable;

/**
 * @author hecvasro
 */
abstract class RecipientBuilder {
  public abstract Uri getImageUri(Context context);
  public abstract String getTitle();

  public abstract Observable<Result> build(String number, String pin);

  static final class Result {
    private final Recipient recipient;
    private final String error;

    Result(Recipient recipient) {
      this.recipient = recipient;
      this.error = null;
    }

    Result(String error) {
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
