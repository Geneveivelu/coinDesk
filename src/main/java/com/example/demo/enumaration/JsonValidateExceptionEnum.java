package com.example.demo.enumaration;

import lombok.Getter;

@Getter
public enum JsonValidateExceptionEnum {

  FORMAT_PARSING_ERROR("Parsing failed, [format] format error"),

  JSON_PARSING_ERROR("Parsing failed, [json] format error"),

  FORMAT_NOT_FOUND("Format not found"),

  FIELD_FORMAT_NOT_FOUND("Field format not found"),

  NESTED_STRUCT_VALIDATE_FAILED("Read nested struct failed");

  private final String errorMessage;

  JsonValidateExceptionEnum(String errorMessage) {
    this.errorMessage = errorMessage;
  }

}
