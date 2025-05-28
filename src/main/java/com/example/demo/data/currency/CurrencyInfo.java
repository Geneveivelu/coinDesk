package com.example.demo.data.currency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CurrencyInfo {

  @NotBlank(message = "code fields cannot be null or empty")
  private String code;

  @NotBlank(message = "symbol fields cannot be null or empty")
  private String symbol;

  @NotBlank(message = "chineseName fields cannot be null or empty")
  private String chineseName;

  @NotNull(message = "rate fields cannot be null or empty")
  private BigDecimal rate;

}
