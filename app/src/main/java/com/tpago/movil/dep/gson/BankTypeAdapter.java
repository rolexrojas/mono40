package com.tpago.movil.dep.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.dep.data.AssetUriBuilder;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.LogoStyle;
import com.tpago.movil.d.domain.LogoUriMap;

import java.io.IOException;

import static com.tpago.movil.dep.Objects.checkIfNull;
import static com.tpago.movil.dep.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
@Deprecated
final class BankTypeAdapter extends TypeAdapter<Bank> {
  private static final String PROPERTY_CODE = "bank-code";
  private static final String PROPERTY_ID = "bank-id";
  private static final String PROPERTY_NAME = "bank-name";
  private static final String PROPERTY_LOGO_URI_TEMPLATE = "bank-logo-uri";

  private final AssetUriBuilder assetUriBuilder;

  BankTypeAdapter(AssetUriBuilder assetUriBuilder) {
    this.assetUriBuilder = assertNotNull(assetUriBuilder, "assetUriBuilder == null");
  }

  @Override
  public Bank read(JsonReader reader) throws IOException {
    Bank bank = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      int code = 0;
      String id = null;
      String name = null;
      String logoUriTemplate = null;
      reader.beginObject();
      while (reader.hasNext()) {
        final String propertyName = reader.nextName();
        if (reader.peek() == JsonToken.NULL) {
          reader.nextNull();
        } else {
          switch (propertyName) {
            case PROPERTY_CODE:
              code = reader.nextInt();
              break;
            case PROPERTY_ID:
              id = reader.nextString();
              break;
            case PROPERTY_NAME:
              name = reader.nextString();
              break;
            case PROPERTY_LOGO_URI_TEMPLATE:
              logoUriTemplate = reader.nextString();
              break;
            default:
              reader.skipValue();
              break;
          }
        }
      }
      reader.endObject();
      final LogoUriMap logoUriMap = LogoUriMap.builder()
        .setUriForGray20(assetUriBuilder.build(logoUriTemplate, LogoStyle.GRAY_20))
        .setUriForGray36(assetUriBuilder.build(logoUriTemplate, LogoStyle.GRAY_36))
        .setUriForPrimary24(assetUriBuilder.build(logoUriTemplate, LogoStyle.PRIMARY_24))
        .setUriForWhite36(assetUriBuilder.build(logoUriTemplate, LogoStyle.WHITE_36))
        .build();
      bank = Bank.builder()
        .setCode(code)
        .setId(id)
        .setName(name)
        .setLogoUriTemplate(logoUriTemplate)
        .setLogoUriMap(logoUriMap)
        .build();
    }
    return bank;
  }

  @Override
  public void write(JsonWriter writer, Bank bank) throws IOException {
    if (checkIfNull(bank)) {
      writer.nullValue();
    } else {
      writer.beginObject();
      writer.name(PROPERTY_CODE);
      writer.value(bank.getCode());
      writer.name(PROPERTY_ID);
      writer.value(bank.getId());
      writer.name(PROPERTY_NAME);
      writer.value(bank.getName());
      writer.name(PROPERTY_LOGO_URI_TEMPLATE);
      writer.value(bank.getLogoUriTemplate());
      writer.endObject();
    }
  }
}
