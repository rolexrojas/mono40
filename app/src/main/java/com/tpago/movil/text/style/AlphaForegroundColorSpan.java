package com.tpago.movil.text.style;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

/**
 * @author hecvasro
 */
public final class AlphaForegroundColorSpan extends ForegroundColorSpan {
  public static final Parcelable.Creator<AlphaForegroundColorSpan> CREATOR
    = new Creator<AlphaForegroundColorSpan>() {
    @Override
    public AlphaForegroundColorSpan createFromParcel(Parcel source) {
      return new AlphaForegroundColorSpan(source);
    }

    @Override
    public AlphaForegroundColorSpan[] newArray(int size) {
      return new AlphaForegroundColorSpan[size];
    }
  };

  private final int alpha;

  private AlphaForegroundColorSpan(Parcel source) {
    super(source);
    this.alpha = source.readInt();
  }

  public AlphaForegroundColorSpan(int color, float alpha) {
    super(color);
    this.alpha = Math.round(255 * alpha);
  }

  @Override
  public void updateDrawState(TextPaint ds) {
    final int color = getForegroundColor();
    ds.setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeInt(alpha);
  }

}
