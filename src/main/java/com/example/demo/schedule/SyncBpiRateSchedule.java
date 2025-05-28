package com.example.demo.schedule;

import com.example.demo.data.coindesk.CoinDeskResponse;
import com.example.demo.exception.HttpRequestException;
import com.example.demo.repository.BpiCurrencyRepository;
import com.example.demo.service.CoinDeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SyncBpiRateSchedule {

  @Autowired
  CoinDeskService coinDeskService;

  @Autowired
  BpiCurrencyRepository bpiCurrencyRepository;

  @Scheduled(cron = "${withdrawCron:0 */5 * * * *}")
  public void syncBpiCurrencyRate() throws HttpRequestException {
    CoinDeskResponse coinDeskResponse = coinDeskService.requestCoinDesk();
  }

}
