package com.example.demo.schedule;

import com.example.demo.data.coindesk.CoinDeskBpiData;
import com.example.demo.data.coindesk.CoinDeskCurrencyData;
import com.example.demo.data.coindesk.CoinDeskResponse;
import com.example.demo.entity.BpiCurrency;
import com.example.demo.exception.HttpRequestException;
import com.example.demo.repository.BpiCurrencyRepository;
import com.example.demo.service.CoinDeskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SyncBpiRateSchedule {

  private static final Logger logger = LogManager.getLogger();

  @Autowired
  CoinDeskService coinDeskService;

  @Autowired
  BpiCurrencyRepository bpiCurrencyRepository;

  @Scheduled(cron = "${withdrawCron:0 */5 * * * *}")
  public void syncBpiCurrencyRate() throws HttpRequestException {
    CoinDeskResponse coinDeskResponse = coinDeskService.requestCoinDesk();
    List<CoinDeskCurrencyData> coinDeskCurrencyDataList = new ArrayList<>();
    CoinDeskBpiData bpiData = coinDeskResponse.getBpi();
    coinDeskCurrencyDataList.add(bpiData.getUsd());
    coinDeskCurrencyDataList.add(bpiData.getGbp());
    coinDeskCurrencyDataList.add(bpiData.getEur());

    for (CoinDeskCurrencyData currencyData : coinDeskCurrencyDataList){
      String code = currencyData.getCode();
      BigDecimal rate = currencyData.getRateFloat();
      logger.info("Schedule update currency rate, {} rate: {}", code, rate);
      BpiCurrency bpiCurrency = bpiCurrencyRepository.findByCode(code);
      if (bpiCurrency != null) {
        bpiCurrency.setRate(rate);
        bpiCurrencyRepository.saveAndFlush(bpiCurrency);
      }
      logger.info("Schedule finish");
    }
  }

}
