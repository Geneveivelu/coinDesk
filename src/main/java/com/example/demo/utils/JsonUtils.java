package com.example.demo.utils;

import com.example.demo.config.LocalDateTimeAdapter;
import com.example.demo.config.ZoneDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public class JsonUtils {

  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(ZonedDateTime.class, new ZoneDateTimeTypeAdapter())
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
      .create();

  public static String toJson(Object object) {
    return gson.toJson(object);
  }

  public static <T> T fromJson(String json, Class<T> classOfT) {
    return gson.fromJson(json, classOfT);
  }

  public static <T> List<T> toList(String content, Class<T> objClass) {
    Type listType = TypeToken.getParameterized(List.class, objClass).getType();
    return gson.fromJson(content, listType);
  }

}
