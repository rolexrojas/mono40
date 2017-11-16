package com.tpago.movil.d.data.api;

import com.tpago.movil.d.domain.api.ApiError;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author hecvasro
 */
@Deprecated
public class ApiErrorTypeAdapter implements JsonDeserializer<ApiError> {
  private static final String PROPERTY_ERROR = "error";
  private static final String PROPERTY_CODE = "code";
  private static final String PROPERTY_DESCRIPTION = "description";

  @Override
  public ApiError deserialize(
    JsonElement json,
    Type typeOfT,
    JsonDeserializationContext context) throws JsonParseException {
    final JsonObject parent = json.getAsJsonObject();
    if (!parent.has(PROPERTY_ERROR)) {
      throw new JsonParseException("Property '" + PROPERTY_ERROR + "' is missing");
    } else {
      final JsonObject child = parent.getAsJsonObject(PROPERTY_ERROR);
      if (!child.has(PROPERTY_CODE)) {
        throw new JsonParseException("Property '" + PROPERTY_CODE + "' is missing");
      } else if (!child.has(PROPERTY_DESCRIPTION)) {
        throw new JsonParseException("Property '" + PROPERTY_DESCRIPTION + "' is missing");
      } else {
        return new ApiError(
          ApiError.Code.fromValue(child.get(PROPERTY_CODE).getAsInt()),
          child.get(PROPERTY_DESCRIPTION).getAsString());
      }
    }
  }
}
