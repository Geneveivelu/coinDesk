package com.example.demo.data.coindesk;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class CoinDeskTimeData {

  public ZonedDateTime updated;

  public ZonedDateTime updatedISO;

  public ZonedDateTime updateduk;

}
