package com.example.demo.data.currency;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SyncCurrencyResponse {

  @JsonFormat
  private LocalDateTime syncTime;

  private List<CurrencyInfo> currencyInfoList;

}
