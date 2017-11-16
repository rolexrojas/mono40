package com.tpago.movil.bank;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.company.TemplateToLogoCatalogMapper;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author hecvasro
 */
public final class BankTypeAdapter extends TypeAdapter<Bank> {

  private static final String PROPERTY_CODE = "bank-code";
  private static final String PROPERTY_ID = "bank-id";
  private static final String PROPERTY_NAME = "bank-name";
  private static final String PROPERTY_LOGO_TEMPLATE = "bank-logo-uri";

  private static final BigDecimal PROPERTY_DEFAULT_TRANSFER_COST_RATE = BigDecimal.valueOf(0.015);

  public static BankTypeAdapter create(TemplateToLogoCatalogMapper templateToLogoCatalogMapper, Gson gson) {
    return new BankTypeAdapter(templateToLogoCatalogMapper, gson);
  }

  private final TemplateToLogoCatalogMapper templateToLogoCatalogMapper;

  private final TypeAdapter<Integer> integerTypeAdapter;
  private final TypeAdapter<String> stringTypeAdapter;

  private BankTypeAdapter(TemplateToLogoCatalogMapper templateToLogoCatalogMapper, Gson gson) {
    this.templateToLogoCatalogMapper = ObjectHelper
      .checkNotNull(templateToLogoCatalogMapper, "templateToLogoCatalogMapper");

    ObjectHelper.checkNotNull(gson, "gson");
    this.integerTypeAdapter = gson.getAdapter(Integer.class);
    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public Bank read(JsonReader reader) throws IOException {
    Bank bank = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      final Bank.Builder builder = Bank.builder()
        .transferCostRate(PROPERTY_DEFAULT_TRANSFER_COST_RATE);
      reader.beginObject();
      while (reader.hasNext()) {
        switch (reader.nextName()) {
          case PROPERTY_CODE:
            builder.code(this.integerTypeAdapter.read(reader));
            break;
          case PROPERTY_ID:
            builder.id(this.stringTypeAdapter.read(reader));
            break;
          case PROPERTY_NAME:
            builder.name(this.stringTypeAdapter.read(reader));
            break;
          case PROPERTY_LOGO_TEMPLATE:
            final String logoTemplate = this.stringTypeAdapter.read(reader);
            builder
              .logoTemplate(logoTemplate)
              .logoCatalog(this.templateToLogoCatalogMapper.apply(logoTemplate));
            break;
        }
      }
      reader.endObject();
      bank = builder.build();
    }
    return bank;
  }

  @Override
  public void write(JsonWriter writer, Bank bank) throws IOException {
    if (ObjectHelper.isNull(bank)) {
      writer.nullValue();
    } else {
      writer.beginObject();
      writer.name(PROPERTY_CODE);
      this.integerTypeAdapter.write(writer, bank.code());
      writer.name(PROPERTY_ID);
      this.stringTypeAdapter.write(writer, bank.id());
      writer.name(PROPERTY_NAME);
      this.stringTypeAdapter.write(writer, bank.name());
      writer.name(PROPERTY_LOGO_TEMPLATE);
      this.stringTypeAdapter.write(writer, bank.logoTemplate());
      writer.endObject();
    }
  }
}
