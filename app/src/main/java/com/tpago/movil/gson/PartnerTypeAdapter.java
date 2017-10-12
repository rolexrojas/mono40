package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tpago.movil.company.LogoCatalogMapper;
import com.tpago.movil.payment.Partner;
import com.tpago.movil.payment.PartnerBuilderFactory;
import com.tpago.movil.util.ObjectHelper;

import java.io.IOException;

/**
 * {@link TypeAdapter Adapter} implementation that transforms {@link Partner partners} to and from
 * JSON.
 *
 * @author hecvasro
 */
final class PartnerTypeAdapter extends TypeAdapter<Partner> {

  private static final String PARTNER_TYPE_CARRIER = "T";
  private static final String PARTNER_TYPE_PROVIDER = "L";

  private static Partner.Type stringToType(String type) {
    return type.equals(PARTNER_TYPE_CARRIER) ? Partner.Type.CARRIER : Partner.Type.PROVIDER;
  }

  private static String typeToString(Partner.Type type) {
    return type == Partner.Type.CARRIER ? PARTNER_TYPE_CARRIER : PARTNER_TYPE_PROVIDER;
  }

  private static final String PROPERTY_TYPE = "partner-type";
  private static final String PROPERTY_CODE = "partner-code";
  private static final String PROPERTY_ID = "partner-id";
  private static final String PROPERTY_NAME = "partner-name";
  private static final String PROPERTY_LOGO_TEMPLATE = "image-url";

  public static PartnerTypeAdapter create(LogoCatalogMapper logoCatalogMapper, Gson gson) {
    return new PartnerTypeAdapter(logoCatalogMapper, gson);
  }

  private final LogoCatalogMapper logoCatalogMapper;

  private final TypeAdapter<Integer> integerTypeAdapter;
  private final TypeAdapter<String> stringTypeAdapter;

  private PartnerTypeAdapter(LogoCatalogMapper logoCatalogMapper, Gson gson) {
    this.logoCatalogMapper = ObjectHelper.checkNotNull(logoCatalogMapper, "logoCatalogMapper");

    ObjectHelper.checkNotNull(gson, "gson");
    this.integerTypeAdapter = gson.getAdapter(Integer.class);
    this.stringTypeAdapter = gson.getAdapter(String.class);
  }

  @Override
  public Partner read(JsonReader reader) throws IOException {
    Partner partner = null;
    if (reader.peek() == JsonToken.NULL) {
      reader.nextNull();
    } else {
      Partner.Type type = null;
      Integer code = null;
      String id = null;
      String name = null;
      String logoTemplate = null;

      reader.beginObject();
      while (reader.hasNext()) {
        final String propertyName = reader.nextName();
        switch (propertyName) {
          case PROPERTY_TYPE:
            type = stringToType(this.stringTypeAdapter.read(reader));
            break;
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

      partner = PartnerBuilderFactory.make(type)
        .code(code)
        .id(id)
        .name(name)
        .logoTemplate(logoTemplate)
        .logoCatalog(this.logoCatalogMapper.apply(logoTemplate))
        .build();
    }
    return partner;
  }

  @Override
  public void write(JsonWriter writer, Partner partner) throws IOException {
    if (ObjectHelper.isNull(partner)) {
      writer.nullValue();
    } else {
      writer.beginObject();
      writer.name(PROPERTY_TYPE);
      this.stringTypeAdapter.write(writer, typeToString(partner.type()));
      writer.name(PROPERTY_CODE);
      this.integerTypeAdapter.write(writer, partner.code());
      writer.name(PROPERTY_ID);
      this.stringTypeAdapter.write(writer, partner.id());
      writer.name(PROPERTY_NAME);
      this.stringTypeAdapter.write(writer, partner.name());
      writer.name(PROPERTY_LOGO_TEMPLATE);
      this.stringTypeAdapter.write(writer, partner.logoTemplate());
      writer.endObject();
    }
  }
}
