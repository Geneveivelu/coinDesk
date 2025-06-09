package com.example.demo.enumaration;

import com.example.demo.exception.JsonValidateException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum JsonValidateFormatEnum {

  STRING("String"),

  INTEGER("Integer"),

  DATE("Date");

  private final String format;

  JsonValidateFormatEnum(String format) {
    this.format = format;
  }

  public static JsonValidateFormatEnum getFormatEnum(String format) throws JsonValidateException {
    for (JsonValidateFormatEnum formatEnum : JsonValidateFormatEnum.values()) {
      if (StringUtils.equals(formatEnum.getFormat(), format)) {
        return formatEnum;
      }
    }
    throw new JsonValidateException(JsonValidateExceptionEnum.FORMAT_NOT_FOUND.getErrorMessage());
  }

}
