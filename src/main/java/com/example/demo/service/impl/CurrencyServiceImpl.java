package com.example.demo.service.impl;

import com.example.demo.data.ResponseObj;
import com.example.demo.data.currency.CurrencyInfo;
import com.example.demo.entity.BpiCurrency;
import com.example.demo.repository.BpiCurrencyRepository;
import com.example.demo.service.CurrencyService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

  private static final Logger logger = LogManager.getLogger();

  @Autowired
  private BpiCurrencyRepository bpiCurrencyRepository;

  private static final String LIKE_SYMBOL = "%";

  @Override
  public ResponseObj queryCurrency(String code) {
    List<BpiCurrency> bpiCurrencyList = bpiCurrencyRepository.findByCodeLikeOrderByCodeAsc(LIKE_SYMBOL + code + LIKE_SYMBOL);
    if (!bpiCurrencyList.isEmpty()) {
      List<CurrencyInfo> currencyInfoList = new ArrayList<>();

      bpiCurrencyList.forEach(currency -> {
        CurrencyInfo currencyInfo = CurrencyInfo.builder()
            .code(currency.getCode())
            .symbol(currency.getSymbol())
            .chineseName(currency.getChineseName())
            .rate(currency.getRate())
            .build();
        currencyInfoList.add(currencyInfo);
      });

      return ResponseObj.newInstance().initSuccessResponse().setResultData(currencyInfoList);
    }
    logger.info("Query BPI_CURRENCY By code= {}, but data not found.", code);
    return ResponseObj.newInstance().initSuccessResponse().setResultMessage(code + " Data not found");
  }

  @Override
  public ResponseObj insertCurrency(CurrencyInfo currencyInfo) {
    LocalDateTime insertDate = LocalDateTime.now();
    String code = currencyInfo.getCode();
    String chineseName = currencyInfo.getChineseName();
    BigDecimal rate = currencyInfo.getRate();

    BpiCurrency bpiCurrency = BpiCurrency.builder()
        .code(code)
        .symbol(currencyInfo.getSymbol())
        .chineseName(chineseName)
        .rate(rate)
        .createDate(insertDate)
        .modifyDate(insertDate)
        .build();

    try {
      bpiCurrencyRepository.saveAndFlush(bpiCurrency);
      logger.info("insert {}, {}, {} success", code, chineseName, rate);
      return ResponseObj.newInstance().initSuccessResponse();
    } catch (Exception e) {
      logger.error("Insert BpiCurrency data failed, exception happened: ", e);
      return ResponseObj.newInstance().initFailResponse().setResultMessage(e.getMessage());
    }
  }

  @Override
  public ResponseObj updateCurrency(CurrencyInfo currencyInfo) {
    String code = currencyInfo.getCode();
    BpiCurrency bpiCurrency = bpiCurrencyRepository.findByCode(code);
    if (bpiCurrency != null) {
      if (compareUpdateBpiCurrency(currencyInfo, bpiCurrency)) {
        LocalDateTime modifyDate = LocalDateTime.now();
        bpiCurrency.setModifyDate(modifyDate);

        bpiCurrencyRepository.saveAndFlush(bpiCurrency);
      } else {
        logger.info("{} Non column update", code);
      }

      return ResponseObj.newInstance().initSuccessResponse();
    } else {
      String errorMessage = code + " data not found!";
      return ResponseObj.newInstance().initFailResponse().setResultMessage(errorMessage);
    }
  }

  @Override
  public ResponseObj deleteCurrency(String code) {
    try {
      bpiCurrencyRepository.deleteByCode(code);
    } catch (Exception e) {
     logger.error("Delete BpiCurrency data failed, exception happened: ", e);
     return ResponseObj.newInstance().initFailResponse().setResultMessage(e.getMessage());
    }
    return ResponseObj.newInstance().initSuccessResponse();
  }

  private boolean compareUpdateBpiCurrency(CurrencyInfo updateCurrencyInfo, BpiCurrency oldBpiCurrency) {
    boolean update = false;
    String code = oldBpiCurrency.getCode();
    if (!StringUtils.equals(updateCurrencyInfo.getChineseName(), oldBpiCurrency.getChineseName())) {
      logger.info("{} update column [ chineseName ]: {}", code, updateCurrencyInfo.getChineseName());
      oldBpiCurrency.setChineseName(updateCurrencyInfo.getChineseName());
      update = true;
    }
    if (!StringUtils.equals(updateCurrencyInfo.getSymbol(), oldBpiCurrency.getSymbol())) {
      logger.info("{} update column [ symbol ]: {}", code, updateCurrencyInfo.getSymbol());
      oldBpiCurrency.setSymbol(updateCurrencyInfo.getSymbol());
      update = true;
    }
    if (updateCurrencyInfo.getRate() != oldBpiCurrency.getRate()) {
      logger.info("{} update column [ rate ]: {}", code, updateCurrencyInfo.getRate());
      oldBpiCurrency.setRate(updateCurrencyInfo.getRate());
      update = true;
    }

    return update;
  }

}
