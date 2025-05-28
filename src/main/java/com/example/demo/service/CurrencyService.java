package com.example.demo.service;

import com.example.demo.data.ResponseObj;
import com.example.demo.data.currency.CurrencyInfo;

public interface CurrencyService {

  ResponseObj queryCurrency(String code);

  ResponseObj insertCurrency(CurrencyInfo currencyInfo);

  ResponseObj updateCurrency(CurrencyInfo currencyInfo);

  ResponseObj deleteCurrency(String code);

}
