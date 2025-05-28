package com.example.demo.service;

import com.example.demo.data.ResponseObj;
import com.example.demo.data.coindesk.CoinDeskResponse;
import com.example.demo.exception.HttpRequestException;

public interface CoinDeskService {

  ResponseObj getCoinDesk();

  ResponseObj reorganizeCoinDeskInfo();

  CoinDeskResponse requestCoinDesk() throws HttpRequestException;

}
