package com.example.demo.data.coindesk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinDeskCurrencyData {

  public String code;

  public String symbol;

  public String rate;

  public String description;

  @JsonProperty("rate_float")
  public BigDecimal rateFloat;

}
