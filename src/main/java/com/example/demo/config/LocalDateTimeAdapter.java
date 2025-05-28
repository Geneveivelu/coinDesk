package com.example.demo.config;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime>  {
  private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
      DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
  );

  @Override
  public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    String dateString = jsonElement.getAsString();
    for (DateTimeFormatter formatter : FORMATTERS) {
      try {
        return LocalDateTime.parse(dateString, formatter);
      } catch (DateTimeParseException ignored) {
      }
    }
    throw new JsonParseException("Deserialize LocalDateTime failed");
  }

  @Override
  public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
    for (DateTimeFormatter formatter : FORMATTERS) {
      try {
        return new JsonPrimitive(localDateTime.format(formatter));
      } catch (DateTimeParseException ignored) {
      }
    }
    throw new JsonParseException("Serialize LocalDateTime Failed");
  }

}
