package com.example.demo.controller;

import com.example.demo.data.ResponseObj;
import com.example.demo.service.CoinDeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coindesk")
public class CoinDeskController {

  @Autowired
  private CoinDeskService coinDeskService;

  @GetMapping("/get")
  public ResponseEntity<ResponseObj> requestCoinDesk() {
    ResponseObj responseBody = coinDeskService.getCoinDesk();
    return ResponseEntity.status(HttpStatus.OK.value()).contentType(MediaType.APPLICATION_JSON).body(responseBody);
  }

  @PostMapping("/reorganize")
  public ResponseEntity<ResponseObj> updateCoinDeskInfo() {

    ResponseObj responseBody = coinDeskService.reorganizeCoinDeskInfo();
    return ResponseEntity.status(HttpStatus.OK.value()).contentType(MediaType.APPLICATION_JSON).body(responseBody);
  }

}
