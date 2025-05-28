package com.example.demo.data.coindesk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CoinDeskBpiData {

  @JsonProperty("USD")
  @SerializedName("USD")
  private CoinDeskCurrencyData usd;

  @JsonProperty("GBP")
  @SerializedName("GBP")
  private CoinDeskCurrencyData gbp;

  @JsonProperty("EUR")
  @SerializedName("EUR")
  private CoinDeskCurrencyData eur;

}
