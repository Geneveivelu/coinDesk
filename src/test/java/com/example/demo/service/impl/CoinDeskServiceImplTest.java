package com.example.demo.service.impl;

import com.example.demo.config.Config;
import com.example.demo.repository.BpiCurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CoinDeskServiceImplTest {

  @Mock
  private Config config;

  @Mock
  private BpiCurrencyRepository bpiCurrencyRepository;

  @InjectMocks
  private CoinDeskServiceImpl coinDeskService;

  @Test
  void getCoinDesk() {
  }

  @Test
  void reorganizeCoinDeskInfo() {
  }

  @Test
  void requestCoinDesk() {
  }
}