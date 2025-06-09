package com.example.demo.enumaration;

import lombok.Getter;

@Getter
public enum JsonValidateErrorEnum {

  TYPE_ERROR("type error","%s must be %s"),

  LOSS_DATA("loss data", "%s");

  private final String error;

  private final String errorMessage;

  JsonValidateErrorEnum(String error, String errorMessage) {
    this.error = error;
    this.errorMessage = errorMessage;
  }

}
