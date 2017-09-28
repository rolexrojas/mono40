package com.tpago.movil.text.style;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

/**
 * @author hecvasro
 */
@Deprecated
public final class AlphaForegroundColorSpan extends ForegroundColorSpan {
  public static final Creator CREATOR = new Creator();

  public static final Property PROPERTY = new Property();

  private float alpha;

  private AlphaForegroundColorSpan(Parcel source) {
    super(source);
    this.alpha = source.readFloat();
  }

  public AlphaForegroundColorSpan(int color) {
    super(color);
    setAlpha(1.0F);
  }

  public final float getAlpha() {
    return alpha;
  }

  public final void setAlpha(float alpha) {
    if (alpha < 0F) {
      alpha = 0F;
    } else if (alpha > 1F) {
      alpha = 1F;
    }
    this.alpha = alpha;
  }

  @Override
  public void updateDrawState(TextPaint ds) {
    ds.setColor(
      Color.argb(
        Math.round(255 * alpha),
        Color.red(getForegroundColor()),
        Color.green(getForegroundColor()),
        Color.blue(getForegroundColor())));
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeFloat(alpha);
  }

  private static final class Creator implements Parcelable.Creator<AlphaForegroundColorSpan> {
    @Override
    public AlphaForegroundColorSpan createFromParcel(Parcel source) {
      return new AlphaForegroundColorSpan(source);
    }

    @Override
    public AlphaForegroundColorSpan[] newArray(int size) {
      return new AlphaForegroundColorSpan[size];
    }
  }

  private static final class Property extends android.util.Property<AlphaForegroundColorSpan, Float> {
    Property() {
      super(Float.class, AlphaForegroundColorSpan.class.getSimpleName());
    }

    @Override
    public Float get(AlphaForegroundColorSpan object) {
      return object.getAlpha();
    }

    @Override
    public void set(AlphaForegroundColorSpan object, Float value) {
      object.setAlpha(value);
    }
  }
}
