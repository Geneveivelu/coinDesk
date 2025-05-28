package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "BPI_CURRENCY", schema = "PUBLIC")
@AllArgsConstructor
@NoArgsConstructor
public class BpiCurrency {

  @Id
  @Column(name = "CODE", unique = true, nullable = false)
  private String code;

  @Column(name = "SYMBOL")
  private String symbol;

  @Column(name = "CHINESE_NAME")
  private String chineseName;

  @Column(name = "RATE")
  private BigDecimal rate;

  @Column(name = "CREATE_DATE", nullable = false)
  private LocalDateTime createDate;

  @Column(name = "MODIFY_DATE", nullable = false)
  private LocalDateTime modifyDate;

}
