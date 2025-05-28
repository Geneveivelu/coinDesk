package com.example.demo.service.impl;

import com.example.demo.repository.BpiCurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

  @Mock
  private BpiCurrencyRepository bpiCurrencyRepository;

  @InjectMocks
  private CurrencyServiceImpl currencyService;

  @Test
  void queryCurrency() {
  }

  @Test
  void insertCurrency() {
  }

  @Test
  void updateCurrency() {
  }

  @Test
  void deleteCurrency() {
  }
}