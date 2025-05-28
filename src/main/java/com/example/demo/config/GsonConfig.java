package com.example.demo.config;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

@Configuration
public class GsonConfig {

  @Bean
  public Gson gson() {
    return new GsonBuilder()
        .registerTypeAdapter(ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
          @Override
          public ZonedDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return ZonedDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString());
          }
        })
        .create();
  }

}
