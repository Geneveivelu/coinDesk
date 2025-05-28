package com.example.demo.data.coindesk;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class CoinDeskBpiData {

  @SerializedName("USD")
  private CoinDeskCurrencyData usd;

  @SerializedName("GBP")
  private CoinDeskCurrencyData gbp;

  @SerializedName("EUR")
  private CoinDeskCurrencyData eur;

}
