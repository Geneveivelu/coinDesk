package com.example.demo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {

  private static final Gson gson = new Gson();

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
