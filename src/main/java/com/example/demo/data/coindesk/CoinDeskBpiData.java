package com.example.demo.data.coindesk;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinDeskBpiData {

  private CoinDeskCurrencyData USD;

  private CoinDeskCurrencyData GBP;

  private CoinDeskCurrencyData EUR;

}
