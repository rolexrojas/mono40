package com.tpago.movil.app.ui.main.transaction.item;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.tpago.movil.app.ui.item.Item;
import com.tpago.movil.util.StringHelper;
import com.tpago.movil.util.function.Action;
import com.tpago.movil.util.ComparisonChain;

@AutoValue
public abstract class IndexItem implements Item, Comparable<IndexItem> {

  public static Class type() {
    return AutoValue_IndexItem.class;
  }

  public static Builder builder() {
    return new AutoValue_IndexItem.Builder();
  }

  IndexItem() {
  }

  @Nullable
  abstract Uri pictureUri();

  abstract String titleText();

  @Nullable
  abstract String subtitleText();

  abstract String actionText();

  abstract Action actionRunner();

  final void onRunAction() {
    this.actionRunner()
      .run();
  }

  @Override
  public int compareTo(@NonNull IndexItem that) {
    return ComparisonChain.create()
      .compare(this.actionText(), that.actionText())
      .compare(this.titleText(), that.titleText())
      .compare(
        StringHelper.emptyIfNull(this.subtitleText()),
        StringHelper.emptyIfNull(that.subtitleText())
      )
      .result();
  }

  @AutoValue.Builder
  public static abstract class Builder {

    Builder() {
    }

    public abstract Builder pictureUri(@Nullable Uri uri);

    public abstract Builder titleText(String text);

    public abstract Builder subtitleText(@Nullable String text);

    public abstract Builder actionText(String text);

    public abstract Builder actionRunner(Action runner);

    public abstract IndexItem build();
  }
}
