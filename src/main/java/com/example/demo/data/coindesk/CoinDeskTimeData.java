package com.example.demo.data.coindesk;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CoinDeskTimeData {

  @JsonFormat(pattern = "MMM d, yyyy HH:mm:ss z", locale = "English")
  public ZonedDateTime updated;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
  public ZonedDateTime updatedISO;

  @JsonFormat(pattern = "MMM d, yyyy 'at' HH:mm z", locale = "English")
  public ZonedDateTime updateduk;

}
