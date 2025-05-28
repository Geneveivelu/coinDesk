package com.example.demo.config;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ZoneDateTimeTypeAdapter implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {
  private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
      DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm:ss z", Locale.ENGLISH),
      DateTimeFormatter.ISO_OFFSET_DATE_TIME,
      DateTimeFormatter.ofPattern("MMM d, yyyy 'at' HH:mm z", Locale.ENGLISH)
  );

  @Override
  public ZonedDateTime deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    String dateString = jsonElement.getAsString();
    for (DateTimeFormatter formatter : FORMATTERS) {
      try {
        return ZonedDateTime.parse(dateString, formatter);
      } catch (DateTimeParseException ignored) {
      }
    }
    throw new JsonParseException("Deserialize ZoneDateTime failed");
  }


  @Override
  public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
    for (DateTimeFormatter formatter : FORMATTERS) {
      try {
        return new JsonPrimitive(zonedDateTime.format(formatter));
      } catch (DateTimeParseException ignored) {
      }
    }
    throw new JsonParseException("Serialize ZoneDateTime Failed");
  }
}
