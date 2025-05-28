package com.example.demo.data.coindesk;

import lombok.Data;

@Data
public class CoinDeskResponse {

  public CoinDeskTimeData time;

  public String disclaimer;

  public String chartName;

  public CoinDeskBpiData bpi;

}
