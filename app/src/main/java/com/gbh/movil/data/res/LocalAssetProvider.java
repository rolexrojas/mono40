package com.gbh.movil.data.res;

import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.gbh.movil.BuildConfig;
import com.gbh.movil.R;
import com.gbh.movil.domain.Bank;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class LocalAssetProvider implements AssetProvider {
  private static final int BANK_ADEMI = 38;
  private static final int BANK_ADOPEM = 44;
  private static final int BANK_ALAVER = 35;
  private static final int BANK_BDI = 36;
  private static final int BANK_LOPEZ_DE_HARO = 37;
  private static final int BANK_POPULAR = 5;
  private static final int BANK_PROGRESO = 24;
  private static final int BANK_RESERVAS = 4;
  private static final int BANK_UNION = 45;

  /**
   * TODO
   */
  public LocalAssetProvider() {
  }

  /**
   * TODO
   *
   * @param drawableId
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  private static Uri createUri(@DrawableRes int drawableId) {
    return Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + drawableId);
  }

  @ColorInt
  @Override
  public int getPrimaryColor(@NonNull Bank bank) {
    @ColorInt int color = 0;
    switch (bank.getCode()) {
      case BANK_ADEMI:
        color = Color.parseColor("#008286");
        break;
      case BANK_ADOPEM:
        color = Color.parseColor("#df4f2a");
        break;
      case BANK_ALAVER:
        color = Color.parseColor("#1d5898");
        break;
      case BANK_BDI:
        color = Color.parseColor("#515251");
        break;
      case BANK_LOPEZ_DE_HARO:
        color = Color.parseColor("#26335D");
        break;
      case BANK_POPULAR:
        color = Color.parseColor("#004990");
        break;
      case BANK_PROGRESO:
        color = Color.parseColor("#0097D7");
        break;
      case BANK_RESERVAS:
        color = Color.parseColor("#294661");
        break;
      case BANK_UNION:
        color = Color.parseColor("#FD4F57");
        break;
    }
    return color;
  }

  @ColorInt
  @Override
  public int getTextColor(@NonNull Bank bank) {
    return Color.WHITE;
  }

  @NonNull
  @Override
  public Uri getLogoUri(@NonNull Bank bank, @Style int style) {
    @DrawableRes int drawableId = -1;
    switch (bank.getCode()) {
      case BANK_ADEMI:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.ademi_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.ademi_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.ademi_36;
        } else {
          drawableId = R.drawable.ademi_36_bln;
        }
        break;
      case BANK_ADOPEM:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.adopem_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.adopem_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.adopem_36;
        } else {
          drawableId = R.drawable.adopem_36_bln;
        }
        break;
      case BANK_ALAVER:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.alaver_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.alaver_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.alaver_36;
        } else {
          drawableId = R.drawable.alaver_36_bln;
        }
        break;
      case BANK_BDI:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.bdi_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.bdi_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.bdi_36;
        } else {
          drawableId = R.drawable.bdi_36_bln;
        }
        break;
      case BANK_LOPEZ_DE_HARO:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.blh_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.blh_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.blh_36;
        } else {
          drawableId = R.drawable.blh_36_bln;
        }
        break;
      case BANK_POPULAR:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.bpd_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.bpd_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.bpd_36;
        } else {
          drawableId = R.drawable.bpd_36_bln;
        }
        break;
      case BANK_PROGRESO:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.banco_del_progreso_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.banco_del_progreso_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.banco_del_progreso_36;
        } else {
          drawableId = R.drawable.banco_del_progreso_36_bln;
        }
        break;
      case BANK_RESERVAS:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.banreservas_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.banreservas_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.banreservas_36;
        } else {
          drawableId = R.drawable.banreservas_36_bln;
        }
        break;
      case BANK_UNION:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.banco_union_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.banco_union_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.banco_union_36;
        } else {
          drawableId = R.drawable.banco_union_36_bln;
        }
        break;
    }
    return drawableId != -1 ? createUri(drawableId) : Uri.EMPTY;
  }
}
