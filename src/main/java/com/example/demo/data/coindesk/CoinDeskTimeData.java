package com.example.demo.data.coindesk;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;

public class CoinDeskTimeData {

  @JsonFormat(pattern = "MMM d, yyyy HH:mm:ss z", locale = "en")
  private ZonedDateTime updated;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
  private ZonedDateTime updatedISO;

  @JsonFormat(pattern = "MMM d, yyyy 'at' HH:mm z", locale = "en")
  private ZonedDateTime updateduk;

}
