package com.example.demo.service.impl;

import com.example.demo.config.Config;
import com.example.demo.data.coindesk.CoinDeskBpiData;
import com.example.demo.data.coindesk.CoinDeskCurrencyData;
import com.example.demo.data.coindesk.CoinDeskResponse;
import com.example.demo.data.ResponseObj;
import com.example.demo.data.currency.CurrencyInfo;
import com.example.demo.data.currency.SyncCurrencyResponse;
import com.example.demo.entity.BpiCurrency;
import com.example.demo.exception.HttpRequestException;
import com.example.demo.repository.BpiCurrencyRepository;
import com.example.demo.service.CoinDeskService;
import com.example.demo.utils.HttpClientUtils;
import com.example.demo.utils.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoinDeskServiceImpl implements CoinDeskService {

  private static final Logger logger = LogManager.getLogger();

  @Autowired
  private Config config;

  @Autowired
  private BpiCurrencyRepository bpiCurrencyRepository;

  @Override
  public ResponseObj getCoinDesk() {
    try {
      CoinDeskResponse coinDeskResponse = requestCoinDesk();
      return ResponseObj.newInstance().initSuccessResponse().setResultData(coinDeskResponse);
    } catch (Exception e) {
      logger.error("Request coinDesk api fail, exception happened: ", e);
      return ResponseObj.newInstance().initFailResponse().setResultMessage(e.getMessage());
    }
  }

  @Override
  public ResponseObj reorganizeCoinDeskInfo() {
    try {
      CoinDeskResponse coinDeskResponse = requestCoinDesk();
      SyncCurrencyResponse syncCurrencyResponse = new SyncCurrencyResponse();
      List<CurrencyInfo> currencyInfoList = new ArrayList<>();
      CoinDeskBpiData bpiData = coinDeskResponse.getBpi();
      currencyInfoList.add(setCurrencyInfo(bpiData.getEur()));
      currencyInfoList.add(setCurrencyInfo(bpiData.getGbp()));
      currencyInfoList.add(setCurrencyInfo(bpiData.getUsd()));

      syncCurrencyResponse.setSyncTime(LocalDateTime.now());
      syncCurrencyResponse.setCurrencyInfoList(currencyInfoList);
      return ResponseObj.newInstance().initSuccessResponse().setResultData(syncCurrencyResponse);
    } catch (Exception e) {
      logger.error("Request coinDesk api fail, exception happened: ", e);
      return ResponseObj.newInstance().initFailResponse().setResultMessage(e.getMessage());
    }
  }

  @Override
  public CoinDeskResponse requestCoinDesk() throws HttpRequestException {
    CoinDeskResponse response = new CoinDeskResponse();
    if (config.useMock) {
      response = getMockCoinDeskResponse();
      logger.info("Get mock coinDesk response, {}", JsonUtils.toJson(response));
    } else {
      HttpResponse<String> responseBody = HttpClientUtils.get(config.coinDeskApiUrl);
      if (responseBody.statusCode() == HttpStatus.OK.value()) {
        logger.info("Request coindesk Api response: {}", responseBody.body());
        response = JsonUtils.fromJson(responseBody.body(), CoinDeskResponse.class);
      } else {
        logger.error("Request coinDesk failed, statusCode: {}", responseBody.statusCode());
        throw new HttpRequestException("Request coinDesk fail");
      }
    }
    return response;
  }

  private CurrencyInfo setCurrencyInfo(CoinDeskCurrencyData currencyData) {
    String code = currencyData.getCode();
    BpiCurrency bpiCurrency = bpiCurrencyRepository.findByCode(code);

    return CurrencyInfo.builder()
        .code(code)
        .symbol(bpiCurrency.getSymbol())
        .chineseName(bpiCurrency.getChineseName())
        .rate(currencyData.getRateFloat())
        .build();
  }

  private CoinDeskResponse getMockCoinDeskResponse() {
    String mockingData = "{" +
        "    \"time\": {" +
        "        \"updated\": \"Aug 3, 2022 20:25:00 UTC\"," +
        "        \"updatedISO\": \"2022-08-03T20:25:00+00:00\"," +
        "        \"updateduk\": \"Aug 3, 2022 at 21:25 BST\"" +
        "    }," +
        "    \"disclaimer\": \"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\"," +
        "    \"chartName\": \"Bitcoin\"," +
        "    \"bpi\": {" +
        "        \"USD\": {" +
        "            \"code\": \"USD\"," +
        "            \"symbol\": \"$\"," +
        "            \"rate\": \"23,342.0112\"," +
        "            \"description\": \"US Dollar\"," +
        "            \"rate_float\": 23342.0112" +
        "        },\n" +
        "        \"GBP\": {" +
        "            \"code\": \"GBP\"," +
        "            \"symbol\": \"£\"," +
        "            \"rate\": \"19,504.3978\"," +
        "            \"description\": \"British Pound Sterling\"," +
        "            \"rate_float\": 19504.3978" +
        "        }," +
        "        \"EUR\": {" +
        "            \"code\": \"EUR\"," +
        "            \"symbol\": \"€\"," +
        "            \"rate\": \"22,738.5269\"," +
        "            \"description\": \"Euro\"," +
        "            \"rate_float\": 22738.5269" +
        "        }" +
        "    }" +
        "}";

    return JsonUtils.fromJson(mockingData, CoinDeskResponse.class);
  }

}
