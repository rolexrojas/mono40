package com.tpago.movil.app.ui;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.tpago.movil.R;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.util.BuilderChecker;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

import java.util.concurrent.atomic.AtomicReference;

@AutoValue
public abstract class AlertData {

  private static AtomicReference<AlertData> GENERIC_FAILURE = new AtomicReference<>();

  private static DialogInterface.OnClickListener createButtonListener(ButtonAction action) {
    return ObjectHelper.isNotNull(action) ? (d, i) -> action.accept() : null;
  }

  public static Builder builder(StringMapper stringMapper) {
    return new Builder(stringMapper);
  }

  public static AlertData createForGenericFailure(StringMapper stringMapper) {
    AlertData data = GENERIC_FAILURE.get();
    if (ObjectHelper.isNull(data)) {
      data = builder(stringMapper)
        .title(R.string.weAreSorry)
        .message(R.string.anUnexpectedErrorOccurred)
        .positiveButton(R.string.ok)
        .build();
      GENERIC_FAILURE.set(data);
    }
    return data;
  }

  public abstract String title();

  public abstract String message();

  public abstract String positiveButtonText();

  @Nullable
  public abstract ButtonAction positiveButtonAction();

  @Nullable
  public abstract String negativeButtonText();

  @Nullable
  public abstract ButtonAction negativeButtonAction();

  @Nullable
  @Memoized
  public DialogInterface.OnClickListener positiveButtonListener() {
    return createButtonListener(this.positiveButtonAction());
  }

  @Nullable
  @Memoized
  public DialogInterface.OnClickListener negativeButtonListener() {
    return createButtonListener(this.negativeButtonAction());
  }

  @Memoized
  @Override
  public abstract String toString();

  @Memoized
  @Override
  public abstract int hashCode();

  interface ButtonAction {

    void accept();
  }

  public static final class Builder {

    private final StringMapper stringMapper;

    private String title;
    private String message;
    private String positiveButtonText;
    private ButtonAction positiveButtonAction;
    private String negativeButtonText;
    private ButtonAction negativeButtonAction;

    private Builder(StringMapper stringMapper) {
      this.stringMapper = stringMapper;
    }

    public final Builder title(String title) {
      this.title = ObjectHelper.checkNotNull(title, "title");
      return this;
    }

    public final Builder title(@StringRes int id) {
      return this.title(this.stringMapper.apply(id));
    }

    public final Builder message(String message) {
      this.message = ObjectHelper.checkNotNull(message, "message");
      return this;
    }

    public final Builder message(@StringRes int id) {
      return this.message(this.stringMapper.apply(id));
    }

    public final Builder positiveButton(String text) {
      this.positiveButtonText = ObjectHelper.checkNotNull(text, "text");
      return this;
    }

    public final Builder positiveButton(@StringRes int id) {
      return this.positiveButton(this.stringMapper.apply(id));
    }

    public final Builder positiveButton(String text, ButtonAction action) {
      this.positiveButtonText = ObjectHelper.checkNotNull(text, "text");
      this.positiveButtonAction = ObjectHelper.checkNotNull(action, "action");
      return this;
    }

    public final Builder positiveButton(@StringRes int id, ButtonAction action) {
      return this.positiveButton(this.stringMapper.apply(id), action);
    }

    public final Builder negativeButton(String text) {
      this.negativeButtonText = ObjectHelper.checkNotNull(text, "text");
      return this;
    }

    public final Builder negativeButton(@StringRes int id) {
      return this.negativeButton(this.stringMapper.apply(id));
    }

    public final Builder negativeButton(String text, ButtonAction action) {
      this.negativeButtonText = ObjectHelper.checkNotNull(text, "text");
      this.negativeButtonAction = ObjectHelper.checkNotNull(action, "action");
      return this;
    }

    public final Builder negativeButton(@StringRes int id, ButtonAction action) {
      return this.negativeButton(this.stringMapper.apply(id), action);
    }

    public final AlertData build() {
      BuilderChecker.create()
        .addPropertyNameIfMissing("title", StringHelper.isNullOrEmpty(this.title))
        .addPropertyNameIfMissing("message", StringHelper.isNullOrEmpty(this.message))
        .addPropertyNameIfMissing(
          "positiveButtonText",
          StringHelper.isNullOrEmpty(this.positiveButtonText)
        )
        .checkNoMissingProperties();

      return new AutoValue_AlertData(
        this.title,
        this.message,
        this.positiveButtonText,
        this.positiveButtonAction,
        this.negativeButtonText,
        this.negativeButtonAction
      );
    }
  }
}
