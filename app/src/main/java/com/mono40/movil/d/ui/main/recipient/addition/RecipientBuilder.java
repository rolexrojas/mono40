package com.mono40.movil.d.ui.main.recipient.addition;

import android.content.Context;
import android.net.Uri;

import com.mono40.movil.d.domain.Recipient;
import com.mono40.movil.util.ObjectHelper;

import rx.Observable;

/**
 * @author hecvasro
 */
abstract class RecipientBuilder {

  public abstract Uri getImageUri(Context context);

  public abstract String getTitle();

  public abstract String getCategoryName();

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
      return ObjectHelper.isNotNull(recipient);
    }

    public final Recipient getData() {
      return recipient;
    }

    public final String getError() {
      return error;
    }
  }
}
