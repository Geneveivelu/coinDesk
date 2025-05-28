package com.example.demo.schedule;

import com.example.demo.data.coindesk.CoinDeskBpiData;
import com.example.demo.data.coindesk.CoinDeskCurrencyData;
import com.example.demo.data.coindesk.CoinDeskResponse;
import com.example.demo.entity.BpiCurrency;
import com.example.demo.exception.HttpRequestException;
import com.example.demo.repository.BpiCurrencyRepository;
import com.example.demo.service.CoinDeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SyncBpiRateSchedule {

  @Autowired
  CoinDeskService coinDeskService;

  @Autowired
  BpiCurrencyRepository bpiCurrencyRepository;

  @Scheduled(cron = "${withdrawCron:0 */5 * * * *}")
  public void syncBpiCurrencyRate() throws HttpRequestException {
    CoinDeskResponse coinDeskResponse = coinDeskService.requestCoinDesk();
    List<CoinDeskCurrencyData> coinDeskCurrencyDataList = new ArrayList<>();
    CoinDeskBpiData bpiData = coinDeskResponse.getBpi();
    coinDeskCurrencyDataList.add(bpiData.getUSD());
    coinDeskCurrencyDataList.add(bpiData.getGBP());
    coinDeskCurrencyDataList.add(bpiData.getEUR());

    for (CoinDeskCurrencyData currencyData : coinDeskCurrencyDataList){
      BpiCurrency bpiCurrency = bpiCurrencyRepository.findByCode(currencyData.getCode());
      bpiCurrency.setRate(currencyData.getRateFloat());
      bpiCurrencyRepository.saveAndFlush(bpiCurrency);
    }
  }

}
