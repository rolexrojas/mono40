package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author hecvasro
 */
final class TypeAdapterBank extends TypeAdapter<Bank> {

  static TypeAdapterBank create(Gson gson) {
    return new TypeAdapterBank(gson);
  }

  private static final String PROPERTY_CODE = "bank-code";
  private static final String PROPERTY_ID = "bank-id";
  private static final String PROPERTY_NAME = "bank-name";
  private static final String PROPERTY_LOGO_TEMPLATE = "bank-logo-uri";

  private static final BigDecimal DEFAULT_TRANSFER_COST_RATE = BigDecimal.valueOf(0.015);

  private static final int CODE_BPD = 5; // Banco Popular Dominicano

  private final TypeAdapter<Integer> integerTypeAdapter;
  private final TypeAdapter<String> stringTypeAdapter;

  private TypeAdapterBank(Gson gson) {
    ObjectHelper.checkNotNull(gson, "gson");
    this.integerTypeAdapter = gson.getAdapter(Integer.class);
    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  private static String toAppName(int code, String name) {
    switch (code) {
      case CODE_BPD:
        return "Popular";
      default:
        return name;
    }
  }

  @Override
  public Bank read(JsonReader reader) throws IOException {
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
      return null;
    }

    Integer code = null;
    String id = null;
    String name = null;
    String logoTemplate = null;
    BigDecimal transferCostRage = DEFAULT_TRANSFER_COST_RATE;

    String propertyName;
    reader.beginObject();
    while (reader.hasNext()) {
      propertyName = reader.nextName();
      switch (propertyName) {
        case PROPERTY_CODE:
          code = this.integerTypeAdapter.read(reader);
          break;
        case PROPERTY_ID:
          id = this.stringTypeAdapter.read(reader);
          break;
        case PROPERTY_NAME:
          name = this.stringTypeAdapter.read(reader);
          break;
        case PROPERTY_LOGO_TEMPLATE:
          logoTemplate = this.stringTypeAdapter.read(reader);
          break;
        default:
          reader.skipValue();
          break;
      }
    }
    reader.endObject();

    return Bank.builder()
      .code(code)
      .id(id)
      .name(toAppName(code, name))
      .logoTemplate(logoTemplate)
      .transferCostRate(transferCostRage)
      .build();
  }

  private static String toApiName(int code, String name) {
    switch (code) {
      case CODE_BPD:
        return "Banco Popular";
      default:
        return name;
    }
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
      this.stringTypeAdapter.write(writer, toApiName(bank.code(), bank.name()));
      writer.name(PROPERTY_LOGO_TEMPLATE);
      this.stringTypeAdapter.write(writer, bank.logoTemplate());
      writer.endObject();
    }
  }
}
