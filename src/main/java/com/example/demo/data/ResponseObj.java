package com.example.demo.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class ResponseObj {

  private boolean success;

  private Integer resultCode;

  private String resultMessage;

  private Object resultData;

  public ResponseObj setSuccess(boolean success) {
    this.success = success;
    return this;
  }

  public ResponseObj setResultCode(Integer resultCode) {
    this.resultCode = resultCode;
    return this;
  }

  public ResponseObj setResultMessage(String resultMessage) {
    this.resultMessage = resultMessage;
    return this;
  }

  public ResponseObj setResultData(Object resultData) {
    this.resultData = resultData;
    return this;
  }

  @JsonIgnore
  public static ResponseObj newInstance() {
    return new ResponseObj();
  }

  @JsonIgnore
  public ResponseObj initSuccessResponse() {
    return this.setSuccess(true).setResultCode(200).setResultMessage("Success");
  }

  @JsonIgnore
  public ResponseObj initFailResponse() {
    return this.setSuccess(false).setResultCode(500).setResultMessage("Server error");
  }

}
