package com.example.demo.controller;

import com.example.demo.data.ResponseObj;
import com.example.demo.data.currency.CurrencyInfo;
import com.example.demo.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

  @Autowired
  private CurrencyService currencyService;

  @PostMapping("/query")
  public ResponseEntity<ResponseObj> queryCurrency(@RequestParam("code") String code) {
    ResponseObj responseBody = currencyService.queryCurrency(code);
    return ResponseEntity.status(HttpStatus.OK.value()).contentType(MediaType.APPLICATION_JSON).body(responseBody);
  }

  @PostMapping("/insert")
  public ResponseEntity<ResponseObj> insertCurrency(@Valid @RequestBody CurrencyInfo currencyInfo, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.OK.value()).contentType(MediaType.APPLICATION_JSON).body(
          ResponseObj.newInstance().setResultCode(40001).setResultMessage(bindingResult.getFieldErrors().toString())
      );
    }
    ResponseObj responseBody = currencyService.insertCurrency(currencyInfo);
    return ResponseEntity.status(HttpStatus.OK.value()).contentType(MediaType.APPLICATION_JSON).body(responseBody);
  }

  @PostMapping("/update")
  public ResponseEntity<ResponseObj> updateCurrency(@Valid @RequestBody CurrencyInfo currencyInfo, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.OK.value()).contentType(MediaType.APPLICATION_JSON).body(
          ResponseObj.newInstance().setResultCode(40001).setResultMessage(bindingResult.getFieldErrors().toString())
      );
    }
    ResponseObj responseBody = currencyService.updateCurrency(currencyInfo);
    return ResponseEntity.status(HttpStatus.OK.value()).contentType(MediaType.APPLICATION_JSON).body(responseBody);
  }

  @PostMapping("/delete")
  public ResponseEntity<ResponseObj> deleteCurrency(@RequestParam("code") String code) {
    ResponseObj responseBody = currencyService.deleteCurrency(code);
    return ResponseEntity.status(HttpStatus.OK.value()).contentType(MediaType.APPLICATION_JSON).body(responseBody);
  }

}
