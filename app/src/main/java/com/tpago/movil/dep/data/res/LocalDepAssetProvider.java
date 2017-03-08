package com.tpago.movil.dep.data.res;

import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import com.tpago.movil.BuildConfig;
import com.tpago.movil.Partner;
import com.tpago.movil.R;
import com.tpago.movil.Bank;

/**
 * @author hecvasro
 */
public final class LocalDepAssetProvider implements DepAssetProvider {
  private static final int BANK_ADEMI = 38;
  private static final int BANK_ADOPEM = 44;
  private static final int BANK_ALAVER = 35;
  private static final int BANK_BDI = 36;
  private static final int BANK_LOPEZ_DE_HARO = 37;
  private static final int BANK_POPULAR = 5;
  private static final int BANK_PROGRESO = 24;
  private static final int BANK_RESERVAS = 4;
  private static final int BANK_UNION = 45;

  private static final int PARTNER_APEC = 28;
  private static final int PARTNER_ARS_HUMANO_NUEVO = 60;
  private static final int PARTNER_ARS_PRIMERA = 61;
  private static final int PARTNER_BOYA = 46;
  private static final int PARTNER_CABLE_BLUESCREEN = 55;
  private static final int PARTNER_CODETEL_CLARO = 7;
  private static final int PARTNER_EDEESTE = 3;
  private static final int PARTNER_EDENORTE = 22;
  private static final int PARTNER_EDESUR = 23;
  private static final int PARTNER_ELECTRICITY_COMPANY = 52;
  private static final int PARTNER_LEIDSA = 48;
  private static final int PARTNER_ORANGE = 27;
  private static final int PARTNER_PGRD_ANTECEDENTES = 56;
  private static final int PARTNER_PGRD_EXEQUATUR = 57;
  private static final int PARTNER_SURA = 43;
  private static final int PARTNER_SEGUROS_BANRESERVAS = 50;
  private static final int PARTNER_SEGUROS_PEPIN = 42;
  private static final int PARTNER_SEG_BANRESERVAS = 31;
  private static final int PARTNER_TELCO_COMPANY = 51;
  private static final int PARTNER_TRICOM_TCN = 26;
  private static final int PARTNER_TRICOM_TELEFONICA = 25;
  private static final int PARTNER_UAPA = 58;
  private static final int PARTNER_UNICARIBE = 53;
  private static final int PARTNER_UNIVERSAL = 32;
  private static final int PARTNER_VIVA = 29;
  private static final int PARTNER_WIND = 33;

  public LocalDepAssetProvider() {
  }

  @NonNull
  private static Uri createUri(@DrawableRes int drawableId) {
    return Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + drawableId);
  }

  @ColorInt
  @Override
  public int getPrimaryColor(@NonNull Bank bank) {
    final String hex;
    switch (bank.getCode()) {
      case BANK_ADEMI:
        hex = "#008286";
        break;
      case BANK_ADOPEM:
        hex = "#DF4F2A";
        break;
      case BANK_ALAVER:
        hex = "#1D5898";
        break;
      case BANK_BDI:
        hex = "#515251";
        break;
      case BANK_LOPEZ_DE_HARO:
        hex = "#26335D";
        break;
      case BANK_POPULAR:
        hex = "#004990";
        break;
      case BANK_PROGRESO:
        hex = "#0097D7";
        break;
      case BANK_RESERVAS:
        hex = "#294661";
        break;
      case BANK_UNION:
        hex = "#FD4F57";
        break;
      default:
        hex = "#D8D8D8";
        break;
    }
    return Color.parseColor(hex);
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
    return drawableId == -1 ? Uri.EMPTY : createUri(drawableId);
  }

  @Override
  public Uri getLogoUri(Partner partner, @Style int style) {
    @DrawableRes int drawableId = -1;
    switch (partner.getCode()) {
      case PARTNER_LEIDSA:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_BOYA:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_ELECTRICITY_COMPANY:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_EDESUR:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.edesur_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.edesur_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.edesur_36;
        } else {
          drawableId = R.drawable.edesur_36_bln;
        }
        break;
      case PARTNER_EDENORTE:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.edenorte_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.edenorte_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.edenorte_36;
        } else {
          drawableId = R.drawable.edenorte_36_bln;
        }
        break;
      case PARTNER_EDEESTE:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.edeeste_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.edeeste_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.edeeste_36;
        } else {
          drawableId = R.drawable.edeeste_36_bln;
        }
        break;
      case PARTNER_TRICOM_TELEFONICA:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.tricom_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.tricom_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.tricom_36_2;
        } else {
          drawableId = R.drawable.tricom_36;
        }
        break;
      case PARTNER_TRICOM_TCN:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_CODETEL_CLARO:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.claro_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.claro_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.claro_36;
        } else {
          drawableId = R.drawable.claro_36_bln;
        }
        break;
      case PARTNER_WIND:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.windtelecom_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.windtelecom_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.windtelecom_36;
        } else {
          drawableId = R.drawable.windtelecom_36_bln;
        }
        break;
      case PARTNER_TELCO_COMPANY:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_ORANGE:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.orange_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.orange_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.orange_36;
        } else {
          drawableId = R.drawable.orange_36_bln;
        }
        break;
      case PARTNER_UNICARIBE:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_UAPA:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_SEGUROS_BANRESERVAS:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.seguros_banreservas_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.seguros_banreservas_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.seguros_banreservas_36;
        } else {
          drawableId = R.drawable.seguros_banreservas_36_bln;
        }
        break;
      case PARTNER_SEG_BANRESERVAS:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_UNIVERSAL:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.seguros_universal_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.seguros_universal_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.seguros_universal_36;
        } else {
          drawableId = R.drawable.seguros_universal_36_bln;
        }
        break;
      case PARTNER_SURA:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.seguros_sura_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.seguros_sura_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.seguros_sura_36;
        } else {
          drawableId = R.drawable.seguros_sura_36_bln;
        }
        break;
      case PARTNER_SEGUROS_PEPIN:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.seguros_pepin_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.seguros_pepin_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.seguros_pepin_36;
        } else {
          drawableId = R.drawable.seguros_pepin_36_bln;
        }
        break;
      case PARTNER_ARS_HUMANO_NUEVO:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.humano_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.humano_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.humano_36;
        } else {
          drawableId = R.drawable.humano_36_bln;
        }
        break;
      case PARTNER_ARS_PRIMERA:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_VIVA:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.viva_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.viva_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.viva_36;
        } else {
          drawableId = R.drawable.viva_36_bln;
        }
        break;
      case PARTNER_APEC:
        if (style == STYLE_20_GRAY) {
          drawableId = R.drawable.unapec_20;
        } else if (style == STYLE_24_PRIMARY) {
          drawableId = R.drawable.unapec_24;
        } else if (style == STYLE_36_GRAY) {
          drawableId = R.drawable.unapec_36;
        } else {
          drawableId = R.drawable.unapec_36_bln;
        }
        break;
      case PARTNER_CABLE_BLUESCREEN:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_PGRD_ANTECEDENTES:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
      case PARTNER_PGRD_EXEQUATUR:
        if (style == STYLE_20_GRAY) {
//          drawableId =
        } else if (style == STYLE_24_PRIMARY) {
//          drawableId =
        } else if (style == STYLE_36_GRAY) {
//          drawableId =
        } else {
//          drawableId =
        }
        break;
    }
    return drawableId == -1 ? Uri.EMPTY : createUri(drawableId);
  }
}
